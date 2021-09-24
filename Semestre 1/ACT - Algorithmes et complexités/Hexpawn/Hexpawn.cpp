#include <iostream>
#include <list>
#include <map>
#include <numeric>
#include <vector>

using namespace std;



int nbLignes, nbColonnes;

typedef pair<vector<string>, char> configuration;
map<configuration, int> damiersRencontres;


/*On représente les cases et pions des joueurs par des characters*/
char joueur_noir  = 'p';
char joueur_blanc = 'P';
char case_vide    = ' ';



/**
 * @brief Vérifie si un joueur a atteint la dernière ligne
 * 
 * @param damier le damier de la partie
 * @param joueur le joueur dont c'est le tour, on veut vérifier s'il a atteint la dernière ligne
 * @return true si le joueur a atteint la dernière ligne (il a gagné la partie)
 * @return false si le joueur n'a pas atteint la dernière ligne (la partie continue)
 */
bool aAtteintLaDerniereLigne(string damier[], char joueur)
{
    if(joueur == joueur_blanc)
        return damier[0].find(joueur_blanc) != string::npos;
    else
        return damier[nbLignes-1].find(joueur_noir) != string::npos;
}



/**
 * @brief Vérifie sur un pion est un adversaire
 * 
 * @param joueurActuel le joueur dont c'est le tour
 * @param adversaire le pion rencontré
 * @return true si le pion rencontré est un adversaire
 * @return false si le pion rencontré est n'est pas un adversaire
 */
bool estUnAdversaire(char joueurActuel, char adversaire)
{
    if(adversaire == case_vide || adversaire == joueurActuel)
        return false;
    else
        return true;    
}



/**
 * @brief Evalue le meilleur entre deux coups selon les critères suivants :
 *            - && -    =    max
 *            - && 0    =    min
 *            0 && 0    =    max
 *            0 && +    =    min
 *            + && +    =    max
 *            + && -    =    max
 * 
 * @param a un int a comparé
 * @param b un int a comparé
 * @return l'int le plus intéressant entre a et b
 */
int eval(int a, int b)
{
    if((a <= 0 && b <= 0) || (a > 0 && b > 0))
        return max(a, b);
    else
        return min(a, b);
}



/**
 * @brief Fais avancer de ligne un joueur
 * 
 * @param joueurActuel le joueur noir ou le joueur blanc dont on veut avancer le pion
 * @return 1 pour le joueur noir et -1 pour le joueur blanc
 */
int avancerJoueur(char joueurActuel)
{
    return joueurActuel == joueur_noir ? 1 : -1;
}

/**
 * @brief Permet de copier un tableau source dans un tableau destination 
 * 
 * @param destination le tableau qui recevra la copie du tableau
 * @param source le tableau qui sera copier
 */
void copieDamier(string destination[], string source[])
{
    /*On copie chaque string du tableau source dans le tableau destination*/
    for(int i = 0; i < nbLignes; i++)
    {
        destination[i] = source[i];
    }
}

/**
 * @brief Jouer le coup pour un pion du damier : avancer, manger a gauche ou a droite
 * 
 * @param damier le damier sur lequel on va jouer
 * @param x coordonnée representant la colonne ou ce situe le pion
 * @param y coordonnée representant la ligne ou ce situe le pion
 * @param centreGaucheDroite l'entier qui permettra en fonction de ca valeur de manger a gauche ou a droite, ou avancer
 * @return retourne une copie du damier donner en parametre, avec le coup jouer
 */
string* jouerCoup(string damier[], int x, int y, int centreGaucheDroite)
{
    string* nouveauDamier = new string[nbLignes];
    /*On créer une copier du damier avec lequel on va jouer le coup*/
    copieDamier(nouveauDamier, damier);

    nouveauDamier[x+avancerJoueur(nouveauDamier[x][y])][y+centreGaucheDroite] = nouveauDamier[x][y];
    nouveauDamier[x][y] = case_vide;
    return nouveauDamier;
}



/**
 * @brief Pour un pion donner en paramétre on essaie de jouer tout les possible par le pion
 * 
 * @param damier le damier sur lequel on va jouer
 * @param x coordonnée representant la colonne ou ce situe le pion
 * @param y coordonnée representant la ligne ou ce situe le pion
 * @param joueurActuel permet de savoir si c'est un pion noir ou blanc
 * @return retourn une liste qui contient des damiers sur lesquelles chacun un coup possible par le pion a etait joué
 */
list<string*> jouerTousLesCoups(string damier[], int x, int y, char joueurActuel)
{
    /*liste qui va contenir chacun des coups possible par le pion, chaque coup est représenté par un damier avec le coup joué*/
    list<string*> evalutations;

    /* si on peut avancer */    
    if((damier[x + avancerJoueur(joueurActuel)][y] == case_vide))
    {
        evalutations.push_back(jouerCoup(damier, x, y, 0));
    }

    /* si on peut manger à gauche */
    if((y > 0) && estUnAdversaire(joueurActuel, damier[x+avancerJoueur(joueurActuel)][y-1]))
    {
        evalutations.push_back(jouerCoup(damier, x, y, -1));
    }

    /* si on peut manger à droite */
    if((y < nbColonnes-1) && estUnAdversaire(joueurActuel, damier[x+avancerJoueur(joueurActuel)][y+1]))
    {
        evalutations.push_back(jouerCoup(damier, x, y, 1));
    }

    return evalutations;
}


/**
 * @brief Permet de jouer tout les coups de tout les pions d'un joueur, afin de connaitre le meilleur coup possible pour un joueur
 * 
 * @param damier le damier sur lequel on va jouer tout les coups de tout les pions
 * @param joueurActuel permet de savoir si on joue tout les pions noir ou blanc
 * @return retourne la valeur du meilleur coup possible
 */
int tour(string damier[], char joueurActuel)
{
     /*Pour mémoriser les diffèrentes configuration, on utilise une map dont la "clé" est une paire composé du (damier et joueur),
     afin d'obtenir la "valeur" qui est le meilleur coup possible pour la paire donnée
    Cela permet de ne pas avoir a recalculer pour une configuration déjà connue*/

    vector<string> damierVecteur;
    
    damierVecteur.assign(damier, damier+nbLignes);
   
    pair<vector<string>, char> configurationActuelle = {damierVecteur, joueurActuel};

    /*On teste si la configuration a déjà était calculé donc connu*/
    map< configuration, int>::iterator itmap = damiersRencontres.find(configurationActuelle);


    /* si le damier actuel est déjà connu*/
    /* on retourne directement le meilleur coup pour cette configuration */
    if(itmap != damiersRencontres.end())
    {
        return itmap-> second;
    }

    /* si l'adversaire a l'issue de son tour a atteint la dernière ligne */
    /* on retourne le nombre de tour nécéssaire pour gagné*/  
    if(aAtteintLaDerniereLigne(damier, (joueurActuel == joueur_blanc ? joueur_noir : joueur_blanc))){
        return 0;
    }
    
    /* on déclare la liste de tous les coups possibles du joueur dont c'est le tour */
    list<string*> nouveauxDamiers;
    /*Liste du noombre de tours pour avant de gagné ou perdre de tous les coups de tous les pions du joueur*/
    list<int> evals;

    /* on va regarder chaque case du damier pour savoir si il y a un pion du joueur et le faire jouer*/
    for(int i = 0; i < nbLignes; i++)
    {
        for(int j = 0; j < nbColonnes; j++)
        {
            /*Si on trouvre un pion du joueur*/
            if(damier[i][j] == joueurActuel)
            {
                /*On fait joué tous les coups possibles par le pion*/
                nouveauxDamiers.splice(nouveauxDamiers.end(), jouerTousLesCoups(damier, i, j, joueurActuel));
            }
        }
    }

    /* si le joueur est bloqué */
    /* il a donc perdu, il y a donc aucun meilleur coup possible donc on renvoie 0 */
    /* sinon */
    /* on retourne la configuration correspondante */
    if(nouveauxDamiers.empty()){
        return 0;
    }
    else
    {

        /*Une fois tout les coups des pions jouer,on fait une liste du nombre de tours nécéssaire avant de perdre au maximum ou de gagné au minimum pour tout les coups jouer*/
        for(string* nouveauDamier : nouveauxDamiers)
        {
            evals.push_back(tour(nouveauDamier, joueurActuel == joueur_noir ? joueur_blanc : joueur_noir));
        }
        auto l_front = evals.begin();
        advance(l_front, 4);
        int premiereValeur = *l_front;
        /*A présent on trie la liste pour connaitre le meilleur coup possible pour tout les coups possible de tout les pions du joueur*/
        int meilleurCoup = accumulate(evals.begin(), evals.end(), premiereValeur, eval);
        
        meilleurCoup = meilleurCoup > 0 ? (meilleurCoup + 1) * -1 : (meilleurCoup - 1) * -1;
        /*Une fois le meilleur coup trouvé on ajoute la configuration et le meilleur coup possible pour cette configuration dans la map*/
        damiersRencontres.insert(make_pair(configurationActuelle, meilleurCoup));

        /*On retourne le meilleur coup possible pour la configuration du damier ce tour*/
        return meilleurCoup;
    }
}



/**
 * @brief Permet de jouer une partie de manière fictif pour connaitre le nombre minimale de tour si on peut gagné la partie ou le nombre maximale de tour si on va perdre la partie
 * 
 * @param damier le damier sur lequel on va jouer la partie
 * @return retourne le nombre minimale de tour si on peut gagné la partie ou le nombre maximale de tour si on va perdre la partie
 */
int partie(string damier[])
{
        return tour(damier, joueur_blanc);
}



int main()
{
    /* on récupère les dimensions du damier */
    cin >> nbLignes >> nbColonnes;
    cin.get();

    /* on initialise notre damier de départ */
    string damierDeDepart[nbLignes];

    /* on récupère le damier de départ */
    for (int i = 0; i < nbLignes; i++)
    {
        getline(cin, damierDeDepart[i]);
        damierDeDepart[i] = damierDeDepart[i].substr(0, nbColonnes);
    }

    /* on lance la partie */    
    cout << partie(damierDeDepart) << endl;

    return 0;
}