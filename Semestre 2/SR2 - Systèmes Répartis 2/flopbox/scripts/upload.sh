read -p '> Press enter to register the localhost server.'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/localhost' --header 'host:localhost'

echo ''
read -p '> Press enter to upload the image.'

echo ''
curl --request POST 'http://localhost:8080/flopbox/localhost/image_uploaded.png/upload' --header 'port:2121' --header 'from:image_to_upload.png' --header 'username:anonymous' --header 'password:anonymous' 

echo ''
read -p '> Press enter to delete the uploaded image and exit the script.'

rm -f image_uploaded.png
