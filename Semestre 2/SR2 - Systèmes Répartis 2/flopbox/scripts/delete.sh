read -p '> Press enter to register the localhost server.'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/localhost' --header 'host:localhost'

echo ''
read -p '> Press enter to create the file and directory to delete.'

touch file_to_delete
mkdir dir_to_delete

echo ''
read -p '> Press enter to delete the file and directory.'

echo ""
curl --request DELETE 'http://localhost:8080/flopbox/localhost/file_to_delete' --header 'port:2121' --header 'username:anonymous' --header 'password:anonymous'
curl --request DELETE 'http://localhost:8080/flopbox/localhost/dir_to_delete'  --header 'port:2121' --header 'username:anonymous' --header 'password:anonymous'

echo ''
read -p '> Press enter to exit the script.'