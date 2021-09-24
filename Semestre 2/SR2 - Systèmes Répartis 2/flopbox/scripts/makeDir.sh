read -p '> Press enter to register the localhost server.'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/localhost' --header 'host:localhost'

echo ''
read -p '> Press enter to create a directory.'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/localhost/dir_created' --header 'port:2121' --header 'username:anonymous' --header 'password:anonymous'

echo ''
read -p '> Press enter to delete the created directory and exit the script.'

rm -rf dir_created
