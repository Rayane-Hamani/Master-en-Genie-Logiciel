#!/bin/bash



usb1pass=$1
usb2pass=$2



# On crée/recrée l'architecture du système

dirNeeded=( usb1 usb2 disk ramdisk )

for dir in ${dirNeeded[*]}
do
    if [ -d $dir ]
    then
        rm -r $dir
    fi

    mkdir $dir
done

echo "Création de l'architecture du système..."



# On crée la base de données de paires nom / n° de carte bancaire

touch ramdisk/database.csv

echo "Création de la base de données..."



# On ajoute des données de démonstration à la base de données

./addPair.sh Edelgard 000
./addPair.sh Edelgard 111
./addPair.sh Edelgard 222
./addPair.sh Hubert 333
./addPair.sh Ferdinand 444
./addPair.sh Linhardt 555
./addPair.sh Caspar 666
./addPair.sh Bernadetta 777
./addPair.sh Dorothea 888
./addPair.sh Petra 999

echo "Ajout de données de démonstration..."



# On génère les parties de la clé de déchiffrement et on les stocke dans des fichiers dans les clés USB

echo `dd if=/dev/urandom bs=64 count=1 status=none | xxd -p -u | tr -d '\n'` > usb1/password.txt
echo `dd if=/dev/urandom bs=64 count=1 status=none | xxd -p -u | tr -d '\n'` > usb2/password.txt

echo "Génération des morceaux de la clé de déchiffrement..."



# On protège ces fichiers par les mots de passe renseignés en arguments de la ligne de commande

openssl enc -aes-256-cbc -salt -pbkdf2 -in usb1/password.txt -out usb1/password.aes -pass pass:$usb1pass
openssl enc -aes-256-cbc -salt -pbkdf2 -in usb2/password.txt -out usb2/password.aes -pass pass:$usb2pass

echo "Cryptage des fichiers contenant les morceaux de la clé de déchiffrement..."



# On supprime les fichiers ayant les parties de la clé de déchiffrement en clair

rm usb1/password.txt
rm usb2/password.txt

echo "Supression des fichiers en clair..."