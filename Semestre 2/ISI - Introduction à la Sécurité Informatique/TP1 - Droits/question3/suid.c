#include <stdio.h>

int main(int argc, char *argv[])
{
    FILE *fptr;
    int ch;
    int UID = getuid();
    int EUID = geteuid();
    int GID = getgid();
    int EGID = getegid();
    printf("UID  : %d\n",UID);
    printf("EUID : %d\n",EUID);
    printf("GID  : %d\n",GID);
    printf("EGID : %d\n",EGID);

    if((fptr = fopen(argv[1],"r")) == NULL){
        printf("Le fichier n'existe pas ou ne permet pas l'ouverture en lecture.\n");
        return -1;
    }


    while((ch = fgetc(fptr)) != EOF){
        printf("%c",ch);
    }

    return 0;
}
