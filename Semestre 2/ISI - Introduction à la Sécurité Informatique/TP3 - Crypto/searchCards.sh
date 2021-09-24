#!/bin/bash



name=$1



# On cherche les numéros de cartes associés à un nom de la base de données

grep $name";" ramdisk/database.csv | while read -r line
do
    echo $line | cut -d ";" -f2
done