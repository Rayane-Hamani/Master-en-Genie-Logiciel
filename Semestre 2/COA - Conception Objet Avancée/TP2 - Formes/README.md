# TP2 : Dessiner des formes géometriques

## Sujet

Le sujet du TP est [ici](sujet/tp2.md). 

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


