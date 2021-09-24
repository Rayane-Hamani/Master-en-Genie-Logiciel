import os
import sys
import json

from collections import OrderedDict

# on importe le fichier json
f_data = open(file=sys.argv[1], mode="r")
data = json.load(fp=f_data, object_pairs_hook=OrderedDict)
i = 0
command=""
verbose=""

def flatten(L):
    res = []
    if(type(L) == type([])):
        for i in L :
            if (type(i) == type([])):
                res = res+flatten(i)
            else :
                res.append(i)
        return res
    return L


def interpreter(i):
    while(i < len(data["program"]["body"])):
        res = expression(data["program"]["body"][i])

        if (isinstance(res, list)):
            execution = ""
            res = flatten(res)
            for r in res:
                execution = execution + str(r) + "\n"
            print("exec block : \n"+execution)
            exec(str(execution))
            print("\n")
        else:
            print("exec : "+str(res))
            exec(str(res))
        i+=1
        
def expression(ex):
    
    if(ex["type"] == "ExpressionStatement"):
        return expression(ex["expression"])



    if(ex["type"] == "AssignmentExpression"):
        return str(expression(ex["left"])) + " " + ex["operator"] + " " + str(expression(ex["right"]))


    
    if(ex["type"] == "NumericLiteral"):
        return ex["value"]


    
    if(ex["type"] == "StringLiteral"):
        return '\"'+ex["value"]+'\"'


    
    if(ex["type"] == "NullLiteral"):
        return "None"


        
    if(ex["type"] == "Identifier"):
        return ex["name"]



    if(ex["type"] == "CallExpression"):
        ret = ex["callee"]["name"] + "("
        if (ex["arguments"]):
            arguments = []
            for a in ex["arguments"]:
                arguments.append(str(expression(a)))
            ret = ret + ",".join(arguments)
        return ret + ")"



    if(ex["type"] == "VariableDeclaration"):
        ret=[]
        for d in ex["declarations"]:
            if(d["init"]):
                ret.append(d["id"]["name"] + " = " + str(expression(d["init"])))
            else:
                ret.append(d["id"]["name"] + " = None")
        return ret



    if(ex["type"] == "WhileStatement"):
        ret = ["while(" + expression(ex["test"]) + "):"]
        for e in ex["body"]["body"]:
            exp = expression(e)
            if (type(exp) == type([])):
                for r in flatten(exp):
                    ret = ret + [("\t"+str(r))]
            else:
                ret.append("\t"+str(expression(e)))
        return ret






    if(ex["type"] == "IfStatement"):
        ret = ["if(" + expression(ex["test"]) + "):"]
        for e in ex["consequent"]["body"]:
            ret.append("\t"+str(expression(e)))
        if(ex["alternate"]):

            ret.append("else:")
            for e in ex["alternate"]["body"]:
                exp = expression(e)
                if (type(exp) == type([])):
                    for r in flatten(exp):
                        ret = ret + [("\t"+str(r))]
                else:
                    ret.append('\t'+str(expression(e)))
        return ret


    if(ex["type"] == "BreakStatement"):
        return "break"


    if(ex["type"] == "ContinueStatement"):
        return "continue"


    if(ex["type"] == "ForStatement"):
        ret = []
        if(ex["init"]):
            ret.append(expression(ex["init"]))
        ret.append("while("+expression(ex["test"])+"):")
        for e in ex["body"]["body"]:
            ret.append("\t"+str(expression(e)))
        ret.append("\t"+str(expression(ex["update"])))
        return ret
        
               


##    if(ex["type"] == "ForStatement"):
##        ret = ["for"]
##        # Variable de boucle
##        # La variable de boucle est definie dans JS
##        if(ex["init"]):
##            var = expression(ex["init"]["left"])
##            exec(var + "=" + expression(ex["init"]["right"]))
##        # Si la variable de boucle n'est pas definie dans JS, on la récupère dans
##        # la partie update du for (meilleure probabilité de tomber sur la bonne
##        # variable. Il faut en revanche ajouter manuellement les differents types
##        # d'expressions qui peuvent se trouver dans la partie update (++, +=, fonction,...)
##        else:
##            if(ex["update"]["type"] == "UpdateExpression"):
##                var = expression(ex["update"]["argument"])
##            elif(ex["update"]["type"] == "AssignmentExpression"):
##                var = expression(ex["update"]["left"])
##            else :
##                print("Type d'update de la variable de boucle non supporté : "+str(ex["update"]["type"]))
##                return None
##            
##        # Condition de boucle (valeur maximale ou minimale de la variable de boucle)
##        # necessite que le test JS ait la variable comme membre de gauche
##        cond = ex["test"]
##        if (cond["operator"] == "<="):
##            minMax = cond["left"]+1
##        elif (cond["operator"] == "=>"):
##            minMax = cond["left"]-1
##        elif(cond["operator"] == ("<" or ">")):
##            minMax = cond["left"]
##        else :
##            print("Condition d'arret de boucle non supportée : "+str(cond["operator"]))
##            return None





    if(ex["type"] == "UpdateExpression"):
        if(ex["operator"] == "++"):
           operation = "+"
        else:
            operation = "-"
        return ex["argument"]["name"] + " " + operation + "= 1"

    
        
    if(ex["type"] == "BinaryExpression"):
        if "extra" in ex:
            if "parenthesized" in ex["extra"]:
                return "("+str(expression(ex["left"]))+" "+ex["operator"]+" "+str(expression(ex["right"]))+")"
        return str(expression(ex["left"]))+" "+ex["operator"]+" "+str(expression(ex["right"]))

    

interpreter(i)
