# TP1 : La classe "Stack"

## Sujet

Le sujet du TP est [ici](sujet/tp1.md). 


## Compilation

Pour ce projet, nous utiliserons l'outil [CMake](https://cmake.org/),
que vous retrouverez sur toutes les plateformes (Windows, Linux, Mac
OS).

Pour configurer et compiler le projet :

- créez un répertoire `build` et lancez la commande `cmake ..`

```bash
mkdir build
cd build
cmake ..
```

La commande `cmake` génère des makefiles à partir des fichier
`CMakeLists.txt` qui sont organisés de manière hierarchique.

- pour compiler, toujours dans le répertoire `build` lancez la commande `make`

- pour tester, toujours dans le répertoire `build` lancez la commande `make test`

- l'exécutable `main` se trouve dans `build/src/`

Il est aussi possible de configurer votre IDE préférée pour utiliser
CMake. Par exemple :

- pour VScode voir [ici](https://devblogs.microsoft.com/cppblog/cmake-tools-extension-for-visual-studio-code/).
- pour Atom, voir [ici](https://atom.io/packages/build-cmake)
- etc.


## Testing

Les tests se trouvent dans le répertoire `test`. Nous utilisons la
librairie [Catch](https://github.com/catchorg/Catch2).  La librairie
consiste d'un seul fichier `catch.hpp` déjà inclus dans le projet
(`test/catch.hpp`).

Pour lancer les tests, après avoir compilé dans le répertoire `build`,
saisir

    make test

ou, la commande

    ./test/test_stack

Si un test ne marche pas, cette dernière commande donne plus
d'informations.

## Écrire des nouveaux tests

Il est important de ne pas toucher au fichier `test/test_main.cpp` : il contient le main de la librairie Catch, et ça prends beaucoup de temps à compiler.

Pour ajouter de nouveaux tests, modifiez le fichier `test/test2.cpp`
ou ajouter des nouveaux fichiers du même type.

Par exemple, vous pouvez ajouter tous les tests sur les opérateurs
dans un fichier `test/test_operators.cpp`, et ajouter ce fichier à la
commande `add_executable` qui se trouve dans le fichier
`test/CMakeLists.txt`, comme indiqué. 








