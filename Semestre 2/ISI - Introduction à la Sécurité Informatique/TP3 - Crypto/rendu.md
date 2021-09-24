# **TP3 d'ISI : Crypto**

## **Binôme**

- Rayane Hamani - rayane.hamani.etu@univ-lille.fr
- Florian Dendoncker - florian.dendoncker.etu@univ-lille.fr

## **Question 1**

Chaque responsable possède une clé usb dans laquelle se trouve un fichier protégé par un mot de passe. Chaque fichier contient 64 caractères hexadécimaux générés aléatoirement.

La clé de déchiffrement est le XOR entre tous les contenus de ces fichiers.

## **Question 2**

- **1.i** --- [Mise en service](puttingIntoService.sh)
- **1.ii** -- [Ajouter une paire](addPair.sh)
- **1.iii** - [Supprimer une paire](removePair.sh)
- **1.iv** -- [Chercher les numéros de cartes associés à un nom](searchCards.sh)
- **1.v** --- [Initialiser les deux clés usb, le disque et le RAM disque](init.sh)

## **Question 3**

Nous avons décidé de faire en sorte que les responsables et leurs représentants aient les mêmes clés.

En effet, s'ils ne possédaient pas les mêmes clés, la combinaison de la clé d'un représentant et d'un responsable engendrerait une clé de déchiffrement différente. Cela ajouterait non seulement de la complexité inutile, mais baisserait aussi fortement la sécurité de notre base de données.

C'est pour cette raison principalement que nous avons décidé de différencier les représentants et les reponsables non pas par leur clé mais par le mot de passe qu'ils utilisent pour accéder à leur clé.

De cette façon, le fonctionnement des services du système n'est pas altéré et il devient possible de distinguer les responsables de leurs représentants via leur mot de passe.

## **Question 4**

Voir le fichier [puttingIntoService.sh](puttingIntoService.sh).

## **Question 5**

Afin de révoquer la responsabilité d'une personne, il faut la remplacer par une nouvelle.

Pour cela, on doit d'abord décrypter la base de données puis ensuite générer une nouvelle partie de clé qui servira à remplacer celle révoquée. Avec cette nouvelle partie de clé, on génère ensuite une nouvelle clé de déchiffrement, toujours avec le XOR entre toutes les parties, et on recrypte la base de données.

## **Question 6**

- **1.vi** -- [Révoquer la responsabilité d'un ayant droit et le remplacer](repudiation.sh)
