echo "Puisque les règles de lambda_a et lambda_b sont symétriques, plutôt que de dupliquer les tests, nous prendre uniquement lambda_a comme référence"

su lambda_a

# Peuvent lire tous les fichiers et sous-répertoires contenus dans dir_a et dir_c

ls -al dir_a
ls -al dir_c

# Peuvent lire mais ne peuvent pas modifier les fichiers dans dir_c, ni les renommer, ni les effacer, ni créer des nouveaux fichiers

rm dir_c/file_c

# Peuvent modifier tous les fichiers contenus dans l'arborescence à partir de dir_a et peuvent créer de nouveaux fichiers et répertoires dans dir_a

touch dir_a/file_a2
mkdir dir_a/dir_a2

# N'ont pas le droit d'effacer, ni de renommer des fichiers dans dir_a qui ne leur appartiennent pas

rm dir_a/file_c_qui_sera_delete

# Ne peuvent pas lire, ni modifier, ni effacer les fichiers dans dir_b et ne peuvent pas créer des nouveaux fichiers dans dir_b

ls -al dir_b    # r
rm dir_b/file_b # w
cd dir_b        # x