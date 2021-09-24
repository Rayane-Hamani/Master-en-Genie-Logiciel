import os
import sys

print("UID = "+str(os.getuid()))
print("EUID = "+str(os.geteuid()))
print("GID = "+str(os.getgid()))
print("EGID = "+str(os.getegid()))

if(os.getgid() != os.stat(sys.argv[1]).st_gid):
   print("The user isn't part of the same group as the file")

else:
    print("Ouverture du fichier passwd")
    passwords = open("/home/ubuntu/imperator/passwd","r")
    print("Fichier ouvert")

    for line in passwords :
        data = line.split("=")
        print("Test : "+data[0]+"="+str(os.getuid())+" ?\n")
        if(data[0] == str(os.getuid())) :
            break

    user_input = input("Password: ")
    if (user_input != data[1].strip()):
        print("Wrong passwod!")
    else:
        os.remove(sys.argv[1])
        print("Fichier supprimé")


