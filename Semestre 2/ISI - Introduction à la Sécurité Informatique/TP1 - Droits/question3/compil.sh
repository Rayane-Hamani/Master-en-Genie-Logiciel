gcc -Wall -c suid.c -o suid.o
gcc -o suid suid.o
rm suid.o
