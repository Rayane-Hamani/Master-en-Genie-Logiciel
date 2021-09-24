#include "stack.h"
#include <iostream>
#include <assert.h>

using namespace std;

void resolve(int nb_disques, Stack *tourD, Stack *tourA, Stack *tourI){
    if(nb_disques != 0){
        
        resolve(nb_disques-1, tourD, tourI, tourA);

        int disque = (*tourD).top();
        *tourA += disque;
        (*tourD).pop();

        cout << "tour 1 : " << *tourD << endl;
        cout << "tour 2 : " << *tourA << endl;
        cout << "tour 3 : " << *tourI << endl << endl;

        resolve(nb_disques-1, tourI, tourA, tourD);
        
    }
}

int main(int argc, char *argv[]){
    
    //  INITIALISATION  //
    Stack tourD;
    Stack tourA;
    Stack tourI;

    assert(argc != 1);                                  // On verifie qu'au moins un argument est entre (on ignore les 2eme et +)
    int nb_disques = atoi(argv[1]);                     // On recupere le nb de disques convertit en int depuis les parametres
    for(int i = nb_disques; i > 0; i--){tourD.push(i);} // On initialise la premiere tour
    //  //  //  //  //  //


    cout << "tour 1 : " << tourD << endl;
    cout << "tour 2 : " << tourA << endl;
    cout << "tour 3 : " << tourI << endl << endl;
    resolve(nb_disques, &tourD, &tourA, &tourI);
    


}



