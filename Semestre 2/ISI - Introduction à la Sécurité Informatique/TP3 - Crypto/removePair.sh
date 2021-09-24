#!/bin/bash



name=$1
number=$2



# On supprime la paire donnée en argument de la base de données

grep -i -x -v $name";"$number ramdisk/database.csv > ramdisk/temp.csv

mv ramdisk/temp.csv ramdisk/database.csv