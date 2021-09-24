#ifndef __STACK_H__
#define __STACK_H__
#include <ostream>

// Exception levée quand on fait top() sur une pile vide.
class EmptyExc{};

// Exception levee lors d'un sqfe push impossible
class IncorrectPush{};

class Stack {  

 public:
    int *pile = nullptr;   // Pointeur vers la pile en elle meme
    int nb = 0;             // Nombre d'elements dans la pile (0 de base à la création d'une pile vide)
    int taille = 20;         // Taille de la pile (20 de base lorsque l'on créé une pile vide)
    
    Stack();               // default constructor, empty stack
    Stack(const Stack &s); // copy constructor
    ~Stack();              // destructor
    
    bool isEmpty() const;  // returns true if empty
    int top() const;       // returns the element at the top
    void pop();            // removes element from the top
    void push(int elem);    // puts an element on top
    void clear();          // removes all elements
    int size() const;      // number of elements currently in the stack
    int maxsize() const;   // size of the internal representation
    void reduce();
    friend std::ostream& operator<<(std::ostream &os, const Stack &s);
    bool operator==(const Stack &other) const;
    Stack &operator=(const Stack &other);
    Stack &operator+=(int elem);

};

#endif
