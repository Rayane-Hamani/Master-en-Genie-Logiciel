read -p '> Press enter to register the ubuntu server (host: ftp.ubuntu.com).'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/ubuntu' --header 'host:ftp.ubuntu.com'

echo ''
read -p '> Press enter to create the directory downloads where the downloaded files will end up.'

mkdir downloads

echo ''
read -p '> Press enter to download the file FOOTER.html from the path cdimage/bionic/daily-live/20200805 of the ubuntu server.'

echo ''
curl --request GET 'http://localhost:8080/flopbox/ubuntu/cdimage/bionic/daily-live/20200805/FOOTER.html/download' --header 'port:21' --header 'username:anonymous' --header 'password:anonymous'

echo ''
read -p '> Press enter to delete the downloaded file and exit the script.'

rm -f downloads/FOOTER.html