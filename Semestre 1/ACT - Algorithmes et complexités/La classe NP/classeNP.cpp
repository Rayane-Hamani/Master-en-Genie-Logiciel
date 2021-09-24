#include <iostream>
#include <unistd.h>
#include <vector>



using namespace std;



/**
 * @brief retourne un message d'aide indiquant comment utiliser le programme
 * 
 * @param nomDuProgramme le nom du programme
 */
void usage (char* nomDuProgramme)
{
    cout << "Liste des options du programme :" << endl
         << "\t-e : Génère tous les certificats possibles jusqu'à en trouver au moins un de valide" << endl
         << "\t-h : Retourne un message d'aide indiquant comment utiliser le programme" << endl
         << "\t-n : Génère un certificat aléatoire et vérifie s'il est valide" << endl
         << "\t-p : Réduit le problème de Partition en JSP (Job Shop Scheduling)" << endl
         << "\t-s : Réduit le problème de Sum en Partition" << endl
         << "\t-v : Lis et vérifie la validité d'un certificat proposé" << endl
         << "\nPour lancer le programme en ligne de commandes, utilisez : " << nomDuProgramme << " [option] [fichier de données] [entier cible dans le cas de l'option -s, rien sinon] [temps d'attente maximum de chaque tâche]" << endl;
}



/**
 * @brief variables globales qui seront utilisées à travers toutes les fonctions
 */
int nbMachines;
int nbTaches;
int tempsDAttenteMaximum;



/**
 * @brief vérifie le certificat proposé est valide ou non
 * 
 * @param certificat le certificat proposé
 * @param taches la liste des tâches
 * @return true si le certificat proposé est valide
 * @return false si le certificat proposé n'est pas valide
 */
bool verification (int certificat[], int taches[][2])
{
    /* on initialise notre nombre de machine libre au début à notre nombre total de machine */
    int nbMachinesLibres = nbMachines;

    /* on assigne ensuite toutes les dates de fin de tâche des machines à 0 */
    int machines[nbMachines] = {};

    /* pour chaque tâche */
    for (int i = 0; i < nbTaches; i++)
    {
        /* si une tâche est lancée avant d'être arrivée
         * le certificat n'est pas valide
         */
        if (certificat[i] < taches[i][0])
            return false;
        
        /* si une tâche a attendu trop longtemps
         * le certificat n'est pas valide
         */
        if (certificat[i] - taches[i][0] > tempsDAttenteMaximum)
            return false;

        /* on libère les machines des tâches terminées
         * si à l'issue de ça, il n'y a toujours aucune machine de libre
         * c'est que le certificat n'est pas valide
         */
        for (int j = 0; j < nbMachines; j++)
        {
            if (machines[j] <= certificat[i])
            {
                machines[j]  = certificat[i] + taches[i][1];
                nbMachinesLibres++;
                break;
            }
        }
        if (nbMachinesLibres == 0)
            return false;

        /* on assigne la tâche à une machine, ce qui fait qu'il y a une machine de libre en moins */
        nbMachinesLibres--;
    }

    /* si à l'issue de tous ces tests, le certificat n'a pas été mis en défaut
     * alors c'est que le certificat est bon
     */
    return true;
}



/**
 * @brief génère aléatoirement un certificat pour des tâches données et vérifie s'il est valide
 * 
 * @param taches la liste des tâches
 * @return true si le certificat aléatoirement généré est valide 
 * @return false si le certificat aléatoirement généré n'est pas valide
 */
bool generateurAleatoire (int taches[][2])
{
    int certificat[nbTaches];
    srand(time(NULL));

    /* pour chaque tâche, génère une date de lancement entre sa date d'arrivée et sa date limite d'exécution */
    for (int i = 0; i < nbTaches; i++)
    {
        int dateDArrivee = taches[i][0];
        int dateLimiteDExecution = dateDArrivee + tempsDAttenteMaximum;
        certificat[i] = rand() % dateLimiteDExecution + dateDArrivee;
    }

    return verification(certificat, taches);
}



/**
 * @brief génère la totalité des certificats pour des tâches données et vérifie si au moins un est valide
 * 
 * @param taches la liste des tâches
 * @return true s'il existe un certificat valide pur les tâches données
 * @return false s'il n'en existe aucun
 */
bool generateurComplet (int taches[][2])
{
    /* on initialise notre tout premier et notre dernier certificat */
    vector<int> premierCertificat;
    vector<int> dernierCertificat;

    for (int i = 0; i < nbTaches; i++)
    {
        premierCertificat.push_back(taches[i][0]);
        dernierCertificat.push_back(taches[i][0] + tempsDAttenteMaximum);
    }

    /* tant que notre premier certificat n'est pas notre dernier */
    /* on l'incrémente en allant du plus petit au plus grand */    
    while (premierCertificat != dernierCertificat)
    {
        premierCertificat[0]++;

        for (int i = 0; i < nbTaches; i++)
        {
            if (premierCertificat[i] >  (tempsDAttenteMaximum + taches[i][0]))
            {
                premierCertificat[i+1] = premierCertificat[i+1] + 1;
                premierCertificat[i] = taches[i][0];
            }
        
            int test[nbTaches];
            copy(premierCertificat.begin(), premierCertificat.end(), test);
            
            if (verification(test, taches)) return true;
        }
    }

    return false;
}



/**
 * @brief réduit une instance de Partition en une instance de JSP
 * 
 * @param taches la liste des tâches
 * @return true si la réduction admet au moins un certificat valide
 * @return false si la réduction n'admet aucun certificat valide
 */
bool reductionPartitionEnJSP (int taches[][2])
{
    /* on force le nombre de machine à 2 (chaque machine accueillera une partition) */
    nbMachines = 2;

    /* on crée une nouvelle liste de tâches composée de la liste de base + deux tâches supplémentaires
     * (toutes les tâches de cette nouvelle liste arrive au temps 0)
     * on calcule le temps total d'exécution des tâches et
     * on donne comme temps d'exécution à ces deux tâches supplémentaire, la moitié de la somme
     */
    int nouvellesTaches[nbTaches+2][2];

    int somme = 0;

    for (int i = 0; i < nbTaches+2; i++)
    {
        nouvellesTaches[i][0] = 0;

        if (i < nbTaches)
        {
            nouvellesTaches[i][1] = taches[i][1];

            somme += taches[i][1];
        }
        else
        {
            nouvellesTaches[i][1] = somme / 2;            
        }
    }

    return generateurComplet(nouvellesTaches);
}



/**
 * @brief réduit une instance de Sum en une instance de Partition
 * 
 * @param taches la liste des tâches
 * @param cible l'entier cible d'une partition (donc la vraie cible sera 2*cible)
 * @return true si la réduction admet au moins un certificat valide
 * @return false si la réduction n'admet aucun certificat valide
 */
bool reductionSumEnPartition (int taches[][2], int cible)
{
    /* la vraie cible */
    cible *= 2;

    /* on calcule le temps total d'exécution des tâches */
    int somme = 0;

    for (int i = 0; i < nbTaches; i++)
    {
        somme += taches[i][1];
    }

    /* si la somme des temps d'exécution des tâches n'est pas égale à la vraie cible
     * on crée une nouvelle liste des tâches composée de la liste de base + une tâche supplémentaire
     * cette tâche supplémentaire possèdera un temps d'exécution égal à la valeur absolue de la différence entre la somme et la vraie cible
     */
    if (somme != cible)
    {
        nbTaches++;
        int nouvellesTaches[nbTaches][2];
        copy(taches[0], taches[nbTaches-1-1], nouvellesTaches[0]);
        nouvellesTaches[nbTaches-1][0] = 0;
        nouvellesTaches[nbTaches-1][1] = abs(somme - cible);
        return reductionPartitionEnJSP(nouvellesTaches);
    }
    
    return reductionPartitionEnJSP(taches);
}



int main (int argc, char **argv)
{
    /* on initialise d'abord le temps d'attente maximum d'une tâche, passé en ligne de commande */
    tempsDAttenteMaximum = stoi(argv[argc-1]);

    /* on récupère ensuite le nombre de machines puis le nombre de tâches */
    cin >> nbMachines >> nbTaches;

    /* on initialise ensuite le tableau de tâches et
     * on récupère la date d'arrivée en 0 puis le temps d'exécution en 1
     */
    int taches[nbTaches][2];
    
    for (int i = 0; i < nbTaches; i++)
    {
        cin >> taches[i][0] >> taches[i][1];
    }

    int option;
    while ((option = getopt(argc, argv, "ehnpsv")) != -1)
    {
        switch (option)
        {
            case 'e' : if (generateurComplet(taches)) cout << "Il existe au moins un certificat de valide" << endl;
                       else                           cout << "Il n'existe aucun certificat de valide" << endl;
                       return EXIT_SUCCESS;

            case 'h' : usage(argv[0]);
                       return EXIT_SUCCESS;

            case 'n' : if (generateurAleatoire(taches)) cout << "Le certificat aléatoirement généré est valide" << endl;
                       else                             cout << "Le certificat aléatoirement généré n'est pas valide" << endl;
                       return EXIT_SUCCESS;

            case 'p' : if (reductionPartitionEnJSP(taches)) cout << "Il existe au moins un certificat de valide" << endl;
                       else                                 cout << "Il n'existe aucun certificat de valide" << endl;
                       return EXIT_SUCCESS;

            case 's' : if (reductionSumEnPartition(taches, stoi(argv[argc-2]))) cout << "Il existe au moins un certificat de valide" << endl;
                       else                                                     cout << "Il n'existe aucun certificat de valide" << endl;
                       return EXIT_SUCCESS;

            case 'v' :  {
                            int certificat[nbTaches];
                            for (int i = 0; i < nbTaches; i++)
                            {
                                cin >> certificat[i];
                            }
                            if (verification(certificat, taches)) cout << "Le certificat est valide" << endl;
                            else                                  cout << "Le certificat est non-valide" << endl;
                            return EXIT_SUCCESS;
                        }

            default  : usage(argv[0]);
                       return EXIT_SUCCESS;
        }
    }

    return EXIT_SUCCESS;
}