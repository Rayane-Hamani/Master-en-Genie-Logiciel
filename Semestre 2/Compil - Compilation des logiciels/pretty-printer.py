import os
import sys
import json

from collections import OrderedDict

# on importe le fichier json
f_data = open(file=sys.argv[1], mode="r")
data = json.load(fp=f_data, object_pairs_hook=OrderedDict)


# on crée puis ouvre le ficher 
file_name = os.path.splitext(os.path.basename(sys.argv[1]))[0]
f_file = open(file=file_name+"-from-json.js", mode="w")


line=1


# xxx
for i in data["tokens"]:


    # Gestion des passages a la ligne
    newline = i["loc"]["start"]["line"]
    if (newline > line):
        f_file.write("\n"*(newline-line))
        line = newline

    # Gestion des blocs de commentaire
    if(isinstance(i["type"],str)):
        if(i["type"] == "CommentBlock"):
            f_file.write("/*"+i["value"]+"*/")

    # Gestion des commentaires mono-lignes
    if(i["type"] == "CommentLine"):
        f_file.write("//" + i["value"])

            
    else:
        # Gestion des numériques
        if(i["type"]["label"] == "num"):
            f_file.write(str(i["value"]))
        # Gestion des opérateurs + et - 
        elif(i["type"]["label"] == "+/-"):
            f_file.write(i["value"])
        # Gestion des noms de variables
        elif(i["type"]["label"] == "name"):
            f_file.write(i["value"])
        # Gestion des chaines de caractères
        elif(i["type"]["label"] == "string"):
            f_file.write('"' + i["value"] + '"')
        # Gestion des opérateurs de comparaison
        elif(i["type"]["label"] == "</>"):
            f_file.write(i["value"] + " ")
        # Gestion des opérateurs d'(in/de)crementation
        elif(i["type"]["label"] == "++/--"):
            f_file.write(i["value"] + " ")
        # Gestion des opérateurs d'(in)égalité
        elif(i["type"]["label"] == "==/!="):
            f_file.write(i["value"] + " ")
        # Gestion des opérateurs d'addition/soustraction a une variable
        elif(i["type"]["label"] == "_="):
            f_file.write(i["value"] + " ")
        # Gestion des EOF (rien n'est a ecrire car le EOF est ajouté automatiquement)
        elif(i["type"]["label"] == "eof"):
            None
        # Gestion de "; * ( ) var = , { } null if else while"
        # ainsi que de tout token non specifié dans les conditions plus haut
        # (ajoute la chaine de caractère représentant le type du token au fichier)
        else:
            f_file.write(i["type"]["label"]+" ")
        

f_data.close()
f_file.close()
