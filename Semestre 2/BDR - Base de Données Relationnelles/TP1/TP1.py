import random
import math
import os
from disque import *

DIR_PATH = os.path.realpath(os.curdir)

def flux_carres(n):
    """Flux des carrés de 0 à n-1"""
    for i in range(n):
        yield i*i

def carres(n):
  res = []
  for i in range(n):
    print("foo")
    res.append(i*i)
  return res

def flux_carres_bis(n):
  for i in range(n):
    print("bar")
    yield i*i

def somme_carres(n):
    """Calcule la somme des carrés de 0 à n-1."""
    res = 0
    for i in flux_carres(n):
        res += i
    return res

def somme_carres_bis(n):
    return sum([i*i for i in range(n)])

def somme_carres_ter(n):
    res = 0
    for k in (i*i for i in range(n)):
        res+=k
    return k

def somme_carres_quad(n):
    return sum(i*i for i in range(n))

def table(descr, nb=10000):
    """Cette fonction génère une séquence de tuples décrits par le dictionnaire
    descr. Le dictionnaire associe à une clé une paire (k,l). La fonction
    génère nb dictionnaires de la manière suivante :
    - chaque clé de descr est une clé de ces dictionnaires
    - à chacune de ces clés x, ces dictionnaires associent un nombre tiré au hasard
      entre k et l lorsque la paire (k,l) est associée à x dans descr.
    NB : cette fonction requiert d'importer le module random.
    """
    for _ in range(nb):
        tuple_res = {}
        for a, (k,l) in descr.items():
            tuple_res[a] = random.randint(min(k, l), max(k, l))
        yield tuple_res

def exemple_table():
    """Exemple d'utilisation de la fonction table. Génère une table de 10 éléments
comportant les attributs 'a' et 'b' et les affiche en flux."""
    schema = {'a': (0,10), 'b': (100,100000)}
    for tuple_tbl in table(schema,nb=10):
        print(tuple_tbl)

def projection(table, champs):
    """
    Renvoie la table (sous forme de flux) obtenue à partir des tuples contenus
    dans ~table~ en n'y conservant que les attributs (les clés) qui sont
    contenus dans ~champs~.

    Renvoie une exception si un attribut de ~champs~ n'est pas un attribut des
    tuples de ~table~.
    TO KEEP
    """
    for i in table:
        res = {}
        for c in champs:
            res[c] = tp[c]
        yield res

def exemple_projection():
    """Exemple d'utilisation de la projection."""
    schema = {'a': (1, 10), 'b': (40, 100), 'c': (20,30)}
    for tuple_res in projection(table(schema ,nb=100),
                                ['a', 'c']):
        print(tuple_res)

def transformation(table, f):
    """Renvoie un flux obtenu en appliquant ~f~ à chacun des tuples composant ~table~.
    """
    for i in table:
            yield f(i)

def exemple_transformation():
    schema = {'a': (1, 10), 'b': (40, 100), 'c': (20,30)}
    f = lambda tp: {'a': tp['a'], 'm': (tp['b']+tp['c'])//2}
    for tuple_res in transformation(table(schema,nb=100), f):
        print(tuple_res)

def projection2(table, champs):
    """
    Renvoie la table (sous forme de flux) obtenue à partir des tuples contenus
    dans ~table~ en n'y conservant que les attributs (les clés) qui sont
    contenus dans ~champs~.

    Renvoie une erreur si un attribut de ~champs~ n'est pas un attribut des
    tuples de ~table~.
    """
    # Ici l'argument t de extract sera fourni par la fonction transformation (les tuples).
    # L'autre information necessaire (champs) est directement récupérée de la fonction parente
    # de extract et n'a donc pas besoin d'être définie comme paramètre de la fonction.
    def extract(t):
        res = {}
        for c in champs :
            res[c] = t[c]
        return res
    
    for r in transformation(table,extract):
        yield(r)

def exemple_projection2():
    """Exemple d'utilisation de la projection."""
    schema = {'a': (1, 10), 'b': (40, 100), 'c': (20,30)}
    for tuple_res in projection2(table(schema ,nb=100),
                                ['a', 'c']):
        print(tuple_res)

def union(t1, t2):
    """Construit un flux qui énumère les éléments de ~t1~ puis ceux de ~t2~."""
    for t in t1:
        yield(t)
    for t in t2:
        yield(t)

def exemple_union():
    """Exemple d'utilisation de la fonction union."""
    schema1 = {'a':(30, 100), 'b': (10, 50)}
    schema2 = {'a': (40, 50), 'n': (100, 200), 'm': (0,10)}
    f = lambda tp: {'a': tp['a']//2, 'b': (tp['m']*tp['m'])//4}
    for tp in union(table(schema1, nb = 10),
                    transformation(table(schema2,nb=10),f)):
        print(tp)

def selection(table, pred):
    """Construit le flux des éléments de ~table~ qui satisfont le prédicat
       ~pred~ (fonction des tuples dans les booléens)."""
    for t in table :
        if (pred(t)):
            yield(t)

def exemple_selection():
    for un_tuple in selection(table({'a': (30, 100), 'b': (10, 50)}, nb=10),
               lambda tp: tp['a'] > 50 and tp['b'] < 45):
        print(un_tuple) 

def selection_index(fichier, idx, valeurs) :
    """
    On suppose que ~fichier~ contient des tuples dont l'une des colonnes est
    indexée par ~idx~. La fonction renvoie le flux des tuples qui associe a la
    colonne indexée une valeur dans la séquence ~valeurs~.

    Attention : si un élément de ~valeurs~ n'est pas référencé dans ~idx~, on
    souhaite qu'il n'y ait pas d'erreur.
    """
    for v in valeurs :
        try:
            for i in trouve_sur_disque(fichier,idx[v]):
                yield i
            break
        except KeyError:
            print("Valeur non existante dans le fichier : "+str(v))

def exemple_selection_index():
    schema = {'a' : (0,10), 'b' : (1,10000000)}
    tbl = table(schema, nb=10000)
    fichier = DIR_PATH + "\\fichier.table"
    mem_sur_disque(tbl, fichier)
    idx = index_fichier(fichier, 'a')
    for tp in selection_index(fichier, idx, range(2,5)):
        print(tp)

def appariement(t1, t2):
    """Renvoie un tuple ayant pour clé les clés de ~t1~ et de ~t2~.

    Lorsqu'une clé n'apparaît que dans un tuple la valeur que lui associe ce
    tuple est celle associée à la clé dans le résultat.

    À une clé qui apparaît dans les deux tuples, le résultat associe la valeur
    que lui associe ~t2~.
    """
    tuple = {}
    for k in t1.keys():
        tuple[k] = t1[k]
    for k in t2.keys():
        tuple[k] = t2[k]
    return tuple

def exemple_appariement():
    t1 = {'a' : 100, 'b' : 50, 'c' : 150}
    t2 = {'c' : 40, 'd' : 10}
    for i in appariement(t1,t2).items():
        print(i)
        
def produit_cartesien(table1, table2):
    """Construit le flux de tuples obtenus en appariant tous les tuples de
    ~table1~ et de ~table2~.

    Ce flux correspond au produit cartésien des deux tables produit par l'algorithme double boucle :
    - ~table1~ est la table utilisée dans le boucle extérieure,
    - ~table2~ est la table utilisée dans la boucle intérieure.
    """
    for t1 in table1:
        for t2 in table2:
            yield(appariement(t1,t2))

def exemple_produit_cartesien():
    schema = {'a' : (0,10), 'b' : (1,10000000)}
    tb1 = table(schema, nb=10)
    schema2 = {'b' : (0,20), 'c' : (1,5)}
    tb2 = table(schema2, nb=10)
    for r in produit_cartesien(tb1,tb2):
        print(r)

# 3.2.1.3
# On obtient pas le nombre voulu de resultat (2 au lieu de 4) car chaque table ne peut etre yield qu'une fois totalement,
# une fois l'information entièrement transmise, on ne peut revenir en arrière.

def produit_cartesien_fichier(fichier1, fichier2):
    """Construit le flux de tuples obtenus en appariant tous les tuples contenus
    dans les fichiers ~fichier1~ et ~fichier2~.

    Ce flux correspond au produit cartésien des deux tables contenues dans les
    fichiers produit par l'algorithme double boucle :
    - la table contenue dans ~fichier1~ est utilisée dans la boucle extérieure,
    - la table contenue dans ~fichier2~ est la table utilisée dans la boucle intérieure.

    """
    for i in lire_sur_disque(fichier1):
        for j in lire_sur_disque(fichier2):
            yield(appariement(i,j))
            
def exemple_produit_cartesien_fichier():
    tb = [table({'a': (1,1)}, nb=2), table({'b': (2,2)}, nb=2)]
    fichiers = [DIR_PATH + "\\fichier.table", DIR_PATH + "\\fichier2.table"]
    for i in range(2): mem_sur_disque(tb[i], fichiers[i])
    for tp in produit_cartesien_fichier(fichiers[0],fichiers[1]): print(tp)

def jointure_theta(fichier1, fichier2, pred):
    """Renvoie le flux des appariements de tuples contenus dans les tables des
    fichiers ~fichier1~ et ~fichier2~" qui satisfont la propriété du prédicat
    ~pred~ (fonction des tuples dans les booléens)."""
    for f in produit_cartesien_fichier(fichier1,fichier2):
        if(pred(f)):
            yield(f)

def exemple_jointure_theta():
    """Créé deux tables de 10 tuples ayant comme valeurs a et b contenus entre 1 et 10.
    Renvoie uniquement les tuples issus de la jointure entre les deux tables dont la somme des valeurs a et b
    est paire"""
    def test(t):
        return(((t['a']+t['b'])%2)==0)
        
    tb = [table({'a': (1,10)}, nb=10), table({'b': (1,10)}, nb=10)]
    # A changer en fonction du chemin a utiliser
    fichiers = [DIR_PATH + "\\fichier.table", DIR_PATH + "\\fichier2.table"]
    for i in range(2): mem_sur_disque(tb[i], fichiers[i])
    for r in jointure_theta(fichiers[0],fichiers[1],test): print(r)

def jointure_naturelle(fichier1, fichier2):
    """Renvoie le flux des tuples de la jointure naturelle des tables contenues
    dans ~fichier1~ et ~fichier2~.

    Il s'agit des appariements des tuples provenant des tables contenues dans
    ~fichier1~et ~fichier2~ qui associent les mêmes valeurs à leurs attributs
    communs.
    """
    for i in lire_sur_disque(fichier1):
        for j in lire_sur_disque(fichier2):
            test = True
            for k in i.keys():
                if (k in j.keys() and i[k] != j[k]):
                    test = False
            if(test):yield appariement(i,j)

def exemple_jointure_naturelle():
    """ Test d'une jointure naturelle entre deux tables, la manière dont les tables sont construite
    fait que le résultat ne peut afficher que des tuples dont la valeur b est contenue entre 5 et 10"""
    tb = [table({'a': (1,10),'b': (5,10)}, nb=30), table({'b': (1,15),'c': (20,25)}, nb=30)]
    fichiers = [DIR_PATH + "\\fichier.table", DIR_PATH + "\\fichier2.table"]
    for i in range(2): mem_sur_disque(tb[i], fichiers[i])
    for r in jointure_naturelle(fichiers[0],fichiers[1]): print(r)

# 3.2.2.3
# On lit plusieurs fois la table dans la boucle interieur (1 fois entierement par element de la table exterieure)
# Si on souhaite limiter le nombre d'accès disque dur du programme, enregistrer la table de la boucle intérerieur est préférable

def jointure_naturelle_mem(fichier1, fichier2):
    """Renvoie le flux des tuples de la jointure naturelle des tables contenues
    dans ~fichier1~ et ~fichier2~.

    Fichier 2 est chargé en mémoire afin qu'il ne soit lu qu'une
    seule fois.

    Il s'agit des appariements des tuples provenant des tables contenues dans
    ~fichier1~et ~fichier2~ qui associent les mêmes valeurs à leurs attributs
    communs.

    """
    data_fichier_2 = []
    for i in lire_sur_disque(fichier2): 
        data_fichier_2.append(i)
        
    for i in lire_sur_disque(fichier1):
        for j in data_fichier_2:
            test = True
            for k in i.keys():
                if (k in j.keys() and i[k] != j[k]):
                    test = False
            if(test):yield appariement(i,j)
                
def exemple_jointure_naturelle_mem():
    """ Test d'une jointure naturelle entre deux tables, la manière dont les tables sont construite
    fait que le résultat ne peut afficher que des tuples dont la valeur b est contenue entre 5 et 10"""
    tb = [table({'a': (1,10),'b': (5,10)}, nb=30), table({'b': (1,15),'c': (20,25)}, nb=30)]
    fichiers = [DIR_PATH + "\\fichier.table", DIR_PATH + "\\fichier2.table"]
    for i in range(2): mem_sur_disque(tb[i], fichiers[i])
    for r in jointure_naturelle_mem(fichiers[0],fichiers[1]): print(r)

def jointure_index(table1, col1, fichier2, index):
    """Renvoie le flux des tuples de la jointure de la ~table1~ et de la table
    contenue dans ~fichier2~ sous la condition que les valeurs de l'attribut
    ~col1~ de ~table1~ soient identiques aux valeurs de l'attribut ~col2~ la
    table de ~fichier2~.

    ~index~ est un index de l'attribut ~col2~ dans ~fichier2~.
    """
    for i in table1:
        try:
            for j in trouve_sur_disque(fichier2,index[i[col1]]):
                yield appariement(i,j)
            break
        except KeyError : print("Valeur non existante dans le fichier2 : "+str(i[col1]))

def exemple_jointure_index():
    # TODO
    yield{}

def jointure_double_index(fichier1, index1, fichier2, index2):
    """Renvoie le flux de tuples obtenue par la jointure des tables contenues dans
     ~fichier1~ et ~fichier2~ avec la condition que les valeurs indexées par
     ~index1~ pour ~table1~ soient égaux aux valeurs indexées par ~index2~ pour
     ~table2~."""
    for i in index1.keys():
        try:
            for j in trouve_sur_disque(fichier1,index1[i]):
                for k in trouve_sur_disque(fichier2,index2[i]):
                    yield appariement(i,j)
            break
        except KeyError : print("Valeur non existante dans le fichier2 : "+str(i))
                
def exemple_jointure_double_index():
    # TODO
    yield{}

def jointure_triee(table1, col1, table2, col2):
    """Implémente la jointure de ~table1~ et ~table2~ sous la condition que les
    valeurs de l'attribut ~col1~ de ~table1~ soient égales aux valeurs de
    l'attribut ~col2~ de ~table2~.

    On suppose que ~table1~ est triée suivant les valeurs croissantes de ~col1
    et que ~table2~ est triée suivant les valeurs croissantes de ~col2~.
    """
    # On initialise les listes de données et les variables temporaires servant a sauvegarder la opremière valeur differente de la valeur actuelle de la liste de données.
    mem1 = []
    mem2 = []
    continue_var = True
    try : 
        temp1 = next(table1)
        temp2 = next(table2)
    except : print("/!\ Une des deux tables est vide ou n'est pas une table.")

    # Fait passer la liste de données correspondant a la table 1 a sa valeur suivante
    # Si on a atteint la fin du flux, on definit la variable temporaire comme egale a 0
    # Cela permet a la prochaine itération d'arreter la boucle du programme
    def next_mem1():
        if(temp1 != 0):
            mem1 = [temp1]
            try:
                i = next(table1)
                while(i[col1] == mem1[0][col1]):
                    mem1.append(i)
                    i = next(table1)
                temp1 = i
            except StopIteration: temp1 = 0
        else : continue_var = False

    # Fait passer la liste de données correspondant a la table 2 a sa valeur suivante
    # Si on a atteint la fin du flux, on definit la variable temporaire comme egale a 0
    # Cela permet a la prochaine itération d'arreter la boucle du programme
    def next_mem2():
        if(temp2 != 0):
            try : 
                mem2 = [temp2]
                i = next(table2)
                while(i[col2] == mem2[0][col2]):
                    mem2.append(i)
                    i = next(table2)
                temp2 = i
            except StopIteration: temp2 = 0
        else : continue_var = False

    # On initialise les deux listes de données (par exemple, pour col1 et col2 = 1)
    next_mem1()
    next_mem2()

    # Si une liste de données est vide, c'est que nous somme arrivé a la fin d'un des flux -> On arrete la boucle
    while(continue_var):

        # Si les valeurs de col1 et col2 ne correspondent pas, on fait avancer une des deux listes dans sa valeur suivante
        if(mem1[0][col1] > mem2[0][col2]):
            next_mem2()
            continue
        elif(mem1[0][col1] < mem2[0][col2]):
            next_mem1()
            continue

        # Pour chaque element des deux listes, on envoie sa fusion
        for i in mem1 :
            for j in mem2 :
                yield appariement(i,j)

        # Une fois toutes les fusions possibles des deux listes envoyées, on fait passer les deux listes de données a leur valeur suivante
        next_mem1()
        next_mem2()

def exemple_jointure_triee():
    # TODO (Comment trier la table ? Elle est créée aléatoirement.)
    yield{}

def minimum_table(table, col):
    """Renvoie la plus petite des valeurs associées à l'attribut ~col~ dans ~table~.
    Si ~table~ est vide, renvoie None.
    """
    try :
        min = next(table)[col]
        for t in table :
            if (t[col] < min) :
                min = t[col]
        return min
    except :
        print("La table est vide")
        return None

def exemple_minimum_table():
    """Exemple d'utilisation de la fonction minimum_table, dans cet exemple particulier, à d'énormes chances de renvoyer 4,
    mais toute valeur entre 4 et 10 est possible."""
    return minimum_table(table({'a': (4,10),'b': (5,10)}, nb=30), 'a')

def moyenne_table(table,col):
    nb_t = 0
    somme = 0
    try :
        nb_t += 1
        somme += next(table)[col]
        for t in table :
            nb_t += 1
            somme += t[col]
        return (somme/nb_t)
    except:
        print("La table est vide")
        return None

def exemple_moyenne_table():
    """Exemple d'utilisation de la fonction moyenne_table.
    La moyenne affichée dans ce cads précis gravite généralement autour de 50."""
    return moyenne_table(table({'a': (1,100),'b': (5,10)}, nb=1000), 'a')

def ecart_type_table(table, col):
    """Renvoie l'écart type des valeurs que les tuples de ~table~ associent à
    l'attribut ~col~.
    Si ~table~ est vide, renvoie None.
    """
    nb_t = 0
    somme = 0
    somme_carré = 0
    try :
        i = next(table)[col]
        nb_t += 1
        somme += i
        somme_carré += i*i
        for t in table :
            nb_t += 1
            somme += t[col]
            somme_carré += t[col]*t[col]
        return math.sqrt((1/nb_t) * (somme_carré-( nb_t*((somme/nb_t)*(somme/nb_t)) )))
    except:
        print("La table est vide")
        return None

def exemple_ecart_type_table():
    """Exemple d'utilisation de la fonction exemple_ecart_type_table
    Dans cet exemple précis, la veleur renvoyée doit avoisinier 29"""
    return ecart_type_table(table({'a': (1,100),'b': (5,10)}, nb=1000), 'a')



