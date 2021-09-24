###### Binome : DENDONCKER Florian / HAMANI Rayane

#   Projet de compilation

Vous utiliserez ce dépôt comme point de départ pour votre projet de
compilation.

##  Compte-rendu

#### Pretty Printer

Un problème de comphréhension du problème a fait que le contenu de l'objet **Tokens** des JSON a été utilisé au lieu de l'objet **Body**, l'interprétation se fait donc element par element et non pas bloc par bloc, l'avantage de cette erreur est que les comentaires sont gérés en simultané du code pour l'affichage (les tokens ne séparent pas les commentaires du reste du code et représentes la forme "brute" du fichier)

Le Pretty-Printer prend en compte les tokens : 

- CommentBlock
- CommentLine
- num
- +/-
- name
- string
- </>
- ++/--
- ==/!=
- +=/-=
- eof
- ;
- *
- ( )
- var
- =
- ,
- { }
- null
- if
- else
- while
- Tout token dont le print n'a besoin que du nom du token en soi

Le Pretty-Printer gère également le passage a la ligne de manière manuelle garder une mise en page similaire au fichier original.

#### Interpreteur

Le langage choisi pour créer l'interpreteur est toujours Python.

L'interpreteur integre : 

- Expressions et Expressions Binaires
- Numérique / String littéral
- Declarations de variables
- Updates (++ --)
- Assignement (= += -=)
- Identifiants
- While
- For
- If
- Break
- Continue
- Definition et appel de fonctions

Probleme interpreteur boucles for  :  La variable i semble semi reset entre les deux for interprétés, la raison est inconnue, on peut voir dans l'exemple ci dessous que le bloc exécuté est correcte, mais le 2ème print semble ne pas renvoyer le résultat attendu, reprenant une variable i avec une valeur de 2.

![image-20210211112528947](./src/reset_var.png)

#### Compilateur

Le langage choisi pour ce projet est encore une fois Python, car il est simple d'utilisateion et permet de rapidement tester la génération de code C via une simple execution dans la console d'une IDE

Quelques problèmes rencontrés dus a ce choix furent :

- Des problèmes de casting : Python cast rarement les variables de lui même, et si un print(5) fonctionnera implicitement, un print(5+"string") demandera d'explicitement caster l'integer pour ne pas causer de problème. Tout le script reposant sur de la concaténation de variable avec des constantes String, cela rend le code plus difficile a lire car des casts trop nombreux peuvent nuire a la lisibilité globale de certaines lignes
- Demander a acceder a une balise non existante (ex : une balise name dans un objet n'en contenant pas) retourne une erreur et non pas juste une valeur null ou vide, il faut donc spécifiquement tester tous les cas ou un doute peut planer quand à la présence d'une balise utilisée.

Des problèmes de compréhension quand au fonctionnement du code assembleur ont beaucoup ralenti le projet a ses début.

Le compilateur est fonctionnel jusqu'aux fonctions, ou un problème de gestion des variables se dévoile, il faut en effet pouvoir gérer les variables au niveau local (dans les fonctions) au lieu de simplement au niveau global pour réussir à gérer les arguments des fonctions, une partie de ce système est en place mais il n'est pas encore complètement fonctionnel.

L'execution du code python affiche a l'interpretation les differents elements rencontrés dans l'ordre d'arrivée, ainsi que les différents lignes de code écrites dans le fichier C afin de fluidifier le debug. Si le compilateur tombe sur un objet qu'il ne peut pas traduire, il ignore son résultat et affiche dans la console le type a ajouter au code pour correctement l'interpréter



Les fonctions implémentées dans le Compilateur sont : 

- Expressions et Expressions Binaires

- Numérique littéral

- Declarations de variables (uniquement globale)

- Updates (++ --)

- Assignement (= += -=)

- Identifiants

- While

- For

- If

- Break

- Continue

- Une partie des appels de fonctions (principalement l'appel de la fonction intégrée print)

- L'assignation de String a des variables (simplement ignorées car compliquer a convertir pour l'assembleur et présent dans uniquement un fichier)

  



