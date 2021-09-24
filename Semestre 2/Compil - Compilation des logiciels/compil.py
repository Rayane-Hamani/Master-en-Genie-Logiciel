import os
import sys
import json

from collections import OrderedDict

debug = False
verbose = True

# on importe le fichier json et on definit les valeurs de base de nos variables de suivi
f_data = open(file=sys.argv[1], mode="r")
data = json.load(fp=f_data, object_pairs_hook=OrderedDict)
variables = []
fonctions = []
nbWhile = 0
nbFor = 0
nbIf = 0
boucle = ""


# on créé le fichier c output et on écrit le début du fichier
d_output = open(file="./output.c", mode="w")
d_output.write('#include "base.h"\n')
d_output.write('int main() {\n')
d_output.write('\tinit(8192, 8192, 8192);\n')

indentation = 1

def compiler():
    i=0
    while(i < len(data["program"]["body"])):
        res = expression(data["program"]["body"][i])
        d_output.write("\n\n")
        for r in res :
            if (verbose) : print(r)
            d_output.write("\t"*indentation)
            d_output.write(r+"\n")
        i = i+1
    d_output.write("return 0;\n}")
    d_output.close()
    f_data.close()

def expression(ex):

    global boucle
    global nbFor
    global nbWhile
    global nbIf

    # Affichage des differents types d'objets trouvés dans le JSON
    if(verbose) : print(str(ex["type"])+"\n\n")

    # Expressions    
    if(ex["type"] == "ExpressionStatement"):
        return expression(ex["expression"])

    # Int seul
    elif(ex["type"] == "NumericLiteral"):
        res = []
        res += ["push(iconst("+str(ex["value"])+"));"]
        # DEBUG
        if(debug):
            res += ["pop(r1);"]
            res += ["debug_reg(r1);"]
        return res


    # Opérations + - * / (pas de gestion des parenthèses)
    elif(ex["type"] == "BinaryExpression"):
        res = []
        #if "extra" in ex:
        #    if "parenthesized" in ex["extra"]:
        #        return "("+str(expression(ex["left"]))+" "+ex["operator"]+" "+str(expression(ex["right"]))+")"
        res += expression(ex["left"])
        res += expression(ex["right"])
        res += ["pop(r2);"]
        res += ["pop(r1);"]
        if(ex["operator"] == "+"):
            res += ["iadd(r1,r2,r1);"]
        elif(ex["operator"] == "-"):
            res += ["isub(r1,r2,r1);"]
        elif(ex["operator"] == "*"):
            res += ["imul(r1,r2,r1);"]
        elif(ex["operator"] == "/"):
            res += ["idiv(r1,r2,r1);"]
        elif(ex["operator"] == "<"):
            res += ["ilt(r1,r1,r2);"]
        elif(ex["operator"] == "<="):
            res += ["ile(r1,r1,r2);"]
        elif(ex["operator"] == ">"):
            res += ["ilt(r1,r2,r1);"]
        elif(ex["operator"] == ">="):
            res += ["ile(r1,r2,r1);"]
        elif(ex["operator"] == "=="):
            res += ["ieq(r1,r2,r1);"]
        elif(ex["operator"] == "!="):
            res += ["ieq(r1,r2,r1);"]
            res += ["lneg(r1,r1);"]
        res += ["push(r1);"]
        # DEBUG
        if(debug):
            res += ["pop(r1);"]
            res += ["debug_reg(r1);"]
        return res

    elif(ex["type"] == "VariableDeclaration"):
        res = []
        for d in ex["declarations"]:
            if(d["init"] and (d["init"]["type"] != "NullLiteral") and(d["init"]["type"] != "StringLiteral")):
                res += ["globals["+str(len(variables))+"] = iconst("+str(d["init"]['value'])+");\n"]
                variables.append(d["id"]["name"])
            else:
                res += ["globals["+str(len(variables))+"] = iconst(0);"]
                variables.append(d["id"]["name"])
        return res

    elif(ex["type"] == "WhileStatement"):
        nbWhileLocal = nbWhile
        nbWhile += 1
        res = []
        boucle = "while"      
        res += ["goto ifwhile"+str(nbWhileLocal)+";"]
        res += ["while"+str(nbWhileLocal)+":"]
        res += expression(ex["body"])
        res += ["ifwhile"+str(nbWhileLocal)+":"]
        res += expression(ex["test"])
        res += ["pop(r1);"]
        res += ["if(asbool(r1)) goto while"+str(nbWhileLocal)+";"]
        res += ["endwhile"+str(nbWhileLocal)+":"]
        return res

    elif(ex["type"] == "BlockStatement"):
        res = []
        for b in ex["body"]:
            res += expression(b)
        return res

    elif(ex["type"] == "ExpressionStatement"):
        return expression(ex["expression"])

    elif(ex["type"] == "UpdateExpression"):
        res = []
        res += expression(ex["argument"])
        res += ["pop(r1);"]
        nom = ex["argument"]["name"]
        if(ex["operator"] == "++"):
            res += ["iadd(r1,r1,iconst(1));"]
        else:
            res += ["isub(r1,r1,iconst(1));"]
        res += ["globals["+str(variables.index(nom))+"] = r1;"]
        return res


    elif(ex["type"] == "Identifier"):
        res = []
        if(ex["name"] in variables):
            res += ["push(globals["+str(variables.index(ex["name"]))+"]);"]
        return res
        

    elif(ex["type"] == "CallExpression"):
        res = []
        nbArguments = 0
        for a in ex["arguments"]:
            nbArguments += 1
            res += expression(a)
        if(ex["callee"]["name"] in fonctions):
            res += ["call(function_"+ex["callee"]["name"]+");"]
            #TODO
            for j in range(nbArguments):
                0
            return []
                
        else :
            if(ex["callee"]["name"] == "print"):
                for j in range(nbArguments):
                    res += ["pop(r1);"]
                    res += ["debug_reg(r1);"]
        return res

    elif(ex["type"] == "IfStatement"):
        nbIfLocal = nbIf
        nbIf += 1
        res = []
        res += expression(ex["test"])
        res += ["pop(r1);"]
        res += ["lneg(r1,r1);"]
        res += ["if(asbool(r1)) goto else"+str(nbIfLocal)+";"]
        res += expression(ex["consequent"])
        res += ["goto endif"+str(nbIfLocal)+";"]
        res += ["else"+str(nbIfLocal)+":"]
        if(ex["alternate"]):
            res += expression(ex["alternate"])
        res += ["endif"+str(nbIfLocal)+":"]
        
        return res

    elif(ex["type"] == "AssignmentExpression"):
        res = []
        res += expression(ex["left"])
        res += expression(ex["right"])
        res += ["pop(r2);"]
        res += ["pop(r1);"]
        if (ex["operator"] == "+="):
            res += ["iadd(r1,r1,r2);"]
        elif(ex["operator"] == "-="):
            res += ["isub(r1,r1,r2);"]
        elif(ex["operator"] == "="):
            res += ["iadd(r1,r2,iconst(0));"]
        res += ["globals["+str(variables.index(ex["left"]["name"]))+"] = r1;"]
        return res        

    elif(ex["type"] == "ForStatement"):
        nbForLocal = nbFor
        nbFor += 1
        res = []
        boucle = "for"
        
        if(ex["init"]):
            res += expression(ex["init"])
        res += ["goto iffor"+str(nbForLocal)+";"]
        res += ["for"+str(nbForLocal)+":"]
        res += expression(ex["body"])
        res += ["nextfor"+str(nbForLocal)+":"]
        res += expression(ex["update"])
        res += ["iffor"+str(nbForLocal)+":"]
        res += expression(ex["test"])
        res += ["pop(r1);"]
        res += ["if(asbool(r1)) goto for"+str(nbForLocal)+";"]
        res += ["endfor"+str(nbForLocal)+":"]
        return res


    elif(ex["type"] == "BreakStatement"):
        if(boucle=="while"):
            return ["goto endwhile"+str(nbWhile-1)+";"]
        elif(boucle=="for"):
            return ["goto endfor"+str(nbFor-1)+";"]

    elif(ex["type"] == "ContinueStatement"):
        if(boucle=="while"):
            return ["goto ifwhile"+str(nbWhile-1)+";"]
        elif(boucle=="for"):
            return ["goto nextfor"+str(nbFor-1)+";"]

    elif(ex["type"] == "MemberExpression"):
        return []

    elif(ex["type"] == "LogicalExpression"):
        return []

    elif(ex["type"] == "EmptyStatement"):
        return []

    elif(ex["type"] == "FunctionDeclaration"):
        res = []
        fonctions.append(ex["id"]["name"])
        res += ["goto endfunction_"+ex["id"]["name"]+";"]
        res += ["function_"+ex["id"]["name"]+":"]
        nombreParams = len(ex["params"])
        #TODO
        for p in ex["params"]:
            0
        return []
            
        
        
        
    

            
            

    else:
        print("MISSING TYPE COVERAGE : "+ex["type"])
        return []

    
        
        
        
        
        
        
        
        


compiler()
    
        
        
    

    
