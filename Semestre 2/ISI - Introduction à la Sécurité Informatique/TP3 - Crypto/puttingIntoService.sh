#!/bin/bash



usb1pass=$1
usb2pass=$2



# On décrypte les fichiers contenant les parties de la clé de déchiffrement

openssl enc -d -aes-256-cbc -salt -pbkdf2 -in usb1/password.aes -out usb1/password.txt -pass pass:$usb1pass
openssl enc -d -aes-256-cbc -salt -pbkdf2 -in usb2/password.aes -out usb2/password.txt -pass pass:$usb2pass

echo "Décryptage des fichiers contenant les morceaux de la clé de déchiffrement..."



# On génère la clé de déchiffrement dans le RAM disk

if [ ! -f logic.bc ]
then
    wget -q http://phodd.net/gnu-bc/code/logic.bc
fi

echo "obase=16;ibase=16;xor(`cat usb1/password.txt`,`cat usb2/password.txt`)" | BC_LINE_LENGTH=0 bc -l logic.bc > ramdisk/key.txt

echo "Génération de la clé de déchiffrement..."



# On crypte la base de données

openssl enc -aes-256-cbc -salt -pbkdf2 -in ramdisk/database.csv -out disk/database.aes -K `cat ramdisk/key.txt`

echo "Cryptage de la base de données..."



# On recrypte les fichiers contenant les clés

openssl enc -aes-256-cbc -salt -pbkdf2 -in usb1/password.txt -out usb1/password.aes -pass pass:$usb1pass
openssl enc -aes-256-cbc -salt -pbkdf2 -in usb2/password.txt -out usb2/password.aes -pass pass:$usb2pass

echo "Cryptage des fichiers contenant les morceaux de la clé de déchiffrement..."



# On supprime les fichiers en clair

rm ramdisk/database.csv
rm ramdisk/key.txt
rm usb1/password.txt
rm usb2/password.txt

echo "Supression des fichiers en clair..."