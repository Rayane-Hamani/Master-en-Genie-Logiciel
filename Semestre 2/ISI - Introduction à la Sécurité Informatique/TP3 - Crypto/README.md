# Regardez le [rendu.md](rendu.md)

Les scripts s'utilisent comme ceux-ci :

- ./init.sh passwordUSB1 passwordUSB2
- ./puttingIntoService.sh passwordUSB1 passwordUSB2
- ./addPair.sh name number
- ./removePair.sh name number
- ./searchCards.sh name
- ./repudiation.sh usb1 passwordUSB1 usb2 passwordUSB2 usb3 passwordUSB3
- ./clean.sh

Si vous rencontrez un problème avec les droits d'exécution d'un script, un petit `chmod +777` sur le script en question et le tour est joué ;)

Pour ./repudiation.sh, créez un dossier usb3 pour que le programme marche as intended

La raison pour laquelle l'encryptage/décryptage du fichier csv retourne un fichier vide, c'est parce-que j'ai fait des tests sur la commande openssl. La logique de notre solution reste la même, seule l'algo de encryptage/décryptage est à changer (en aes-256-cbc ou autre)
