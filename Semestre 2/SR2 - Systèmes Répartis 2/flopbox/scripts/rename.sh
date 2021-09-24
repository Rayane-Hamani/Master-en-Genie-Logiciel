read -p '> Press enter to register the localhost server.'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/localhost' --header 'host:localhost'

echo ''
read -p '> Press enter to create the file and directory to rename.'

touch file_to_rename
mkdir dir_to_rename

echo ''
read -p '> Press enter to rename the file and directory.'

echo ""
curl --request PATCH 'http://localhost:8080/flopbox/localhost/file_to_rename' --header 'port:2121' --header 'to:file_renamed' --header 'username:anonymous' --header 'password:anonymous'
curl --request PATCH 'http://localhost:8080/flopbox/localhost/dir_to_rename'  --header 'port:2121' --header 'to:dir_renamed'  --header 'username:anonymous' --header 'password:anonymous'

echo ''
read -p '> Press enter to delete the file and directory and exit the script.'

rm -f  file_renamed
rm -rf dir_renamed
