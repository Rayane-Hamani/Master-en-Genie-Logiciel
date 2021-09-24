read -p '> Press enter to register the ubuntu server (host: ftp.ubuntu.com).'

echo ''
curl --request PUT 'http://localhost:8080/flopbox/ubuntu' --header 'host:ftp.ubuntu.com'

echo ''
read -p '> Press enter to get the raw list of the root directory of the ubuntu server.'

echo ''
curl --request GET 'http://localhost:8080/flopbox/ubuntu/' --header 'port:21' --header 'username:anonymous' --header 'password:anonymous'

echo ''
read -p '> Press enter to get the raw list of the file FOOTER.html from the path cdimage/bionic/daily-live/20200805 of the ubuntu server.'

echo ''
curl --request GET 'http://localhost:8080/flopbox/ubuntu/cdimage/bionic/daily-live/20200805/FOOTER.html' --header 'port:21' --header 'username:anonymous' --header 'password:anonymous'

echo ''
read -p '> Press enter to exit the script.'