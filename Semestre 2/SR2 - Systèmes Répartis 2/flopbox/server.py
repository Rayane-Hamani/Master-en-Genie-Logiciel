# The code below is from : https://pyftpdlib.readthedocs.io/en/latest/tutorial.html

# I have slightly modified it to serve my software.
# Use it if you encounter permission issues with pyftpdlib.



import os

from pyftpdlib.authorizers import DummyAuthorizer
from pyftpdlib.handlers import FTPHandler
from pyftpdlib.servers import FTPServer

def main():
    # Instantiate a dummy authorizer for managing 'virtual' users
    authorizer = DummyAuthorizer()

    # Define a new user and an anonymous user, both having full r/w permissions
    authorizer.add_user('rayane', 'hamani', '.', perm='elradfmwMT') # user
    authorizer.add_anonymous(os.getcwd(), perm='elradfmwM')         # anonymous

    # Instantiate FTP handler class
    handler = FTPHandler
    handler.authorizer = authorizer

    # Define a customized banner (string returned when client connects)
    handler.banner = "pyftpdlib based ftpd ready."

    # Specify a masquerade address and the range of ports to use for
    # passive connections. Decomment in case you're behind a NAT.
    #handler.masquerade_address = '151.25.42.11'
    #handler.passive_ports = range(60000, 65535)

    # Instantiate FTP server class and listen on 0.0.0.0:2121
    address = ('', 2121)
    server = FTPServer(address, handler)

    # Set a limit for connections
    server.max_cons = 1024
    server.max_cons_per_ip = 256

    # Start ftp server
    server.serve_forever()



if __name__ == '__main__':
    main()