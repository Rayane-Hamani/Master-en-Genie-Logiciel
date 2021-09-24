# Creation des utilisateurs et affectation des groupes

sudo adduser lambda_a
sudo addgroup groupe_a
sudo usermod -g groupe_a lambda_a

sudo adduser lambda_b
sudo addgroup groupe_b
sudo usermod -g groupe_b lambda_b

sudo adduser governator
sudo usermod -g ubuntu governator
sudo usermod -a -G groupe_a governator
sudo usermod -a -G groupe_b governator

# Mise en places des règles

su lambda_a
mkdir dir_a
chmod -777 dir_a
chmod +770 dir_a
chmod +t dir_a
touch dir_a/file_a

su lambda_b
mkdir dir_b
chmod -777 dir_b
chmod +770 dir_b
chmod +t dir_b
touch dir_b/file_b

su governator
mkdir dir_c
chmod -777 dir_c
chmod +775 dir_c
touch dir_c/file_c
touch dir_a/file_c
chmod +t dir_c
touch dir_a/file_c_qui_sera_delete