su governator

# Peuvent lire tous les fichiers et sous-répertoires contenus dans dir_a et dir_c

ls -al dir_a
ls -al dir_c

# Peuvent lire, modifier, renommer, effacer et créer des fichiers dans dir_c

ls -al dir_c    # r
rm dir_c/file_c # w
cd dir_c        # x
cd ..

# Peuvent modifier tous les fichiers contenus dans l'arborescence à partir de dir_a et peuvent créer de nouveaux fichiers et répertoires dans dir_a

touch dir_a/file_a2
mkdir dir_a/dir_a2

# N'ont pas le droit d'effacer, ni de renommer des fichiers dans dir_a qui ne leur appartiennent pas

rm dir_a/file_a

# Peuvent lire, modifier, renommer, effacer et créer des fichiers dans dir_b

ls -al dir_b    # r
rm dir_b/file_b # w
cd dir_b        # x
cd ..