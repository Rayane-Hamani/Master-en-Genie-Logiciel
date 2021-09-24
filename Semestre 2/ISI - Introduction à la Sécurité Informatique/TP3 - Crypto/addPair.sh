#!/bin/bash



name=$1
number=$2



# On ajoute la paire donnée en argument à la base de données

echo $name";"$number >> ramdisk/database.csv