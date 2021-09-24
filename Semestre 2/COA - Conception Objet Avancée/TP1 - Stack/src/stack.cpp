#include <stack.h>
#include <iostream>

/**
   Complete the member functions below
 */

Stack::Stack(){
    pile = new int[taille];
}

Stack::Stack(const Stack &s){
    taille = s.taille;
    pile = new int[taille];
    for (int i = 0; i < s.nb; i++) {
        pile[i] = s.pile[i];
    }
    nb = s.nb;
}

Stack::~Stack(){
    delete [] pile;
}
    
bool Stack::isEmpty() const{
    return(nb == 0);
}

int Stack::top() const {
    if(isEmpty()){throw EmptyExc{};}
    else{
        return(pile[nb - 1]);
    }
}

void Stack::pop(){
    if(!isEmpty()){nb--;}
}

void Stack::push(int elem){
    if(nb == taille){
        int *temp = new int[taille+20];
        for(int i = 0; i < taille; i++){temp[i] = pile[i];}
        delete [] pile;
        pile = temp;
        taille += 20;
    }
    pile[nb] = elem;
    nb++;
}

void Stack::clear(){
    nb = 0;
}

int Stack::size() const{
    return(nb);
}

int Stack::maxsize() const{
    return(taille);
}

void Stack::reduce(){
    int *temp = new int[size()];
    for(int i = 0; i < size(); i++){
        temp[i] = pile[i];
    }
    delete pile;
    pile = temp;
    taille = size();
}

std::ostream& operator<<(std::ostream &os, const Stack &s){
    os << "[";
    for(int i = s.nb - 1; i > -1; i--){
        os << s.pile[i];
        if(i != 0){os << ",";}
    }
    os << "]";
    return(os);
}

bool Stack::operator==(const Stack &other) const{
    if(nb != other.nb){return(false);}
    for(int i = 0; i < nb; i++){
        if(pile[i] != other.pile[i]){return(false);}
    }
    return(true);

}

Stack& Stack::operator=(const Stack &other){
    if(this == &other){
        return *this;
    }
    int *temp = new int[other.maxsize()];
    for(int i = 0; i < other.maxsize(); i++){
        temp[i] = other.pile[i];
    }
    nb = other.size();
    taille = other.maxsize();
    delete pile;
    pile = temp;
}

Stack& Stack::operator+=(int elem){
    if(isEmpty() || top() > elem){
        push(elem);
        return(*this);
    }
    throw IncorrectPush{};
}

