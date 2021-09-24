

# Instructions

Vous devez rendre sur gitlab le code demandé avec un fichier RENDU.md
contenant :

-   Vos noms
-   Pour chaque question
    -   Si vous êtes réussi à coder les fonctionnalité demandées
    -   La liste de tests unitaires correspondants à la question


# La classe Stack

Vous devez concevoir et coder une classe `Stack`. La classe
représente une pile qui contient des entiers. L'interface de la
classe est la suivante :

    class Stack {
        // champs à remplir
    public:
        Stack();               // default constructor, empty stack
        Stack(const Stack &s); // copy constructor
        ~Stack();              // destructor
    
        bool isEmpty() const;  // returns true if empty
        int top() throws EmptyExc const;  // returns the element at the top
        void pop();            // removes element from the top
        void push(int elem);   // puts an element on top
        void clear();          // removes all elements
        int size() const;      // number of elements currently in the stack
        int maxsize() const;   // size of the internal representation
    };

Quand la pile est vide : 

-   si l'utilisateur essaie d'appeler `top()`, une exception est levée;
-   si l'utilisateur essaie d'appeler `pop()`, il n'y a pas
    d'exception, l'opération ne fait rien.

La classe n'a pas, à priori, une limite supérieure au nombre
d'éléments qu'elle peut contenir. 

La représentation interne est un tableau d'entiers de taille
fixe. Si la pile est pleine, et on essaie d'insérer un nouveau
élément,

1.  un nouveau tableau, plus grand, est crée ;
2.  le contenu de l'ancien tableau est copié dans le nouveau ;
3.  l'ancien tableau est détruit.


## Question 1: coder la classe

Coder la classe. Testez votre code avec les tests unitaires qui
sont fourni dans le répertoire `test/`. Si tous les tests passent,
lors d'un `git push` le voyant dans l'interface git-lab passe de
rouge à vert.


# Première opérateur

Pour pouvoir imprimer facilement le contenu d'un pile, c'est utile
de coder l'opérateur de sortie : 

    std::ostream& operator<<(std::ostream &os, const Stack &s);  

On utilise cette opérateur comme dans le code suivant : 

    Stack s;
    s.push(1);
    s.push(2);
    s.push(3);
    cout << s << endl;

\noindent et la sortie sera la suivante : 

    [3, 2, 1]  

(dans l'ordre inverse d'insertion). 


## Question 2: operateur de sortie

Coder l'opérateur global

    std::ostream& operator<<(std::ostream &os, const Stack &s);

Une fois codé, ajoutez un nouveau test au répertoire `test` pour
vérifier qu'il marche bien. Pour tester cette opérateur, vous
pouvez utiliser un `stringstream` comme sortie ([help](http://www.cplusplus.com/reference/sstream/stringstream/stringstream/)) à la place
de `cout`.


## Question 3: operateur de comparaison

Coder l'opérateur membre: 

    bool operator==(const Stack &other) const;

avec la sémantique la plus intuitive (il retourne `true` si les
deux piles contient les même éléments dans le même ordre, et
`false` si non).

Écrire un test unitaire pour vérifier le bon fonctionnement
de l'opérateur.

Maintenant, à l'aide de vos nouveaux opérateurs, c'est plus facile
de tester le code de la pile.


# Test unitaires


## Question 4: constructeur par copie

Écrire d'autres tests unitaires. 
En particulier, il faut tester :

1.  le bon fonctionnement du *copy-constructor* ;
2.  le fait que la taille s'agrandi de manière automatique quand on
    pousse des nouveaux éléments.
3.  Qu'on arrive toujours à récupérer les éléments dans l'ordre
    inverse d'insertion.


## Question 5: operateur d'affectation

Coder l'opérateur d'affectation :

    Stack &operator=(const Stack &other);

Cette opérateur doit donner les même résultats que le constructeur
par copie. Écrire un test pour vérifier cette équivalence. 


## Question 6: reduction de la taille mémoire

Ajouter une méthode membre `void reduce()` qui réduit la taille du
tableau pour la rendre égal au nombre d'éléments contenus dans la
pile.

Vérifiez le bon fonctionnement de cette fonction avec des nouveaux
tests unitaires.


# Exercice: Les tours de Hanoï

Utiliser la classe Stack pour coder l'algorithme de résolution des
tours de Hanoï avec `n` disques.

-   Voir la description du problème sur [Wikipedia](https://fr.wikipedia.org/wiki/Tours_de_Hano%25C3%25AF#Solution_r%25C3%25A9cursive).

-   Chaque disque est représenté par un entier qui donne sa
    dimension : le disque plut petit est représenté par le nombre 1,
    le disque plus grand par le nombre 8.

-   Chaque tour est représentée par un Stack.


## Question 7: safe push

Ajouter à la classe Stack l'opérateur membre suivant :

    Stack &operator+=(int elem);

qui verifie si le push est correct en comparant le paramètre
`elem` avec le top du `Stack` : si `elem` est plus petit que le
top du stack, il fait le `push` ; si non, il lève une exception de
type `IncorrectPush`.

Testez l'operateur. 


## Question 8: coder algorithme

Coder dans le fichier `src/main.cpp` le programme qui prend en
entrée le nombre de disques, et montre la solution pour 3 tours
d'Hanoï sur le terminal, en utilisant l'algorithme récursive
décrit dans Wikipedia.

Utiliser l'opérateur += defini à la question précedente pour
insérer un disque sur une tour.

