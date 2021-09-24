#!/bin/bash



cur=$1
curPass=$2

old=$3
oldPass=$4

new=$5
newPass=$6



# On décrypte les parties de la clé de déchiffrement

openssl enc -d -aes-256-cbc -salt -pbkdf2 -in $cur/password.aes -out $cur/password.txt -pass pass:$curPass
openssl enc -d -aes-256-cbc -salt -pbkdf2 -in $old/password.aes -out $old/password.txt -pass pass:$oldPass

echo "Décryptage des fichiers contenant les morceaux de la clé de déchiffrement..."



# On génère la clé de déchiffrement

echo "obase=16;ibase=16;xor(`cat $cur/password.txt`,`cat $old/password.txt`)" | BC_LINE_LENGTH=0 bc -l logic.bc > ramdisk/key.txt

echo "Génération de la clé de déchiffrement..."



# On décrypte la base de données

openssl enc -d -aes-256-cbc -salt -pbkdf2 -in disk/database.aes -out ramdisk/database.csv -K `cat ramdisk/key.txt`

echo "Décryptage de la base de données..."



# On génère une nouvelle partie de clé

echo `dd if=/dev/urandom bs=32 count=1 status=none | xxd -p -u | tr -d '\n'` > $new/password.txt

echo "Génération du nouveau morceau de la clé de déchiffrement..."



# On regénère la clé de déchiffrement

echo "obase=16;ibase=16;xor(`cat $cur/password.txt`,`cat $new/password.txt`)" | BC_LINE_LENGTH=0 bc -l logic.bc > ramdisk/key.txt

echo "Génération de la nouvelle clé de déchiffrement..."



# On recrypte la base de données

openssl enc -aes-256-cbc -salt -pbkdf2 -in ramdisk/database.csv -out disk/database.aes -K `cat ramdisk/key.txt`

echo "Cryptage de la base de données..."



# On recrypte les parties de la clé de déchiffrement

openssl enc -aes-256-cbc -salt -pbkdf2 -in $cur/password.txt -out $cur/password.aes -pass pass:$curPass
openssl enc -aes-256-cbc -salt -pbkdf2 -in $new/password.txt -out $new/password.aes -pass pass:$newPass

echo "Cryptage des fichiers contenant les morceaux de la nouvelle clé de déchiffrement..."



# On supprime le dossier contenant l'ancienne partie de clé

rm ramdisk/database.csv
rm ramdisk/key.txt
rm $cur/password.txt
rm $new/password.txt
rm -r $old

echo "Supression des fichiers en clair..."


