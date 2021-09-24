package flopbox.client;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import flopbox.command.ServerCommands;



public class ClientUtil
{
	/**
	 * Connect and log in the FTP server.
	 * 
	 * @param client The FTP client.
	 * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
	 * 
	 * @return True if the client could connect and log in the FTP server, otherwise false.
	 */
	public static boolean connectAndLogin(FTPClient client, String server, int port, String username, String password)
	{
		try
		{
			client.connect(ServerCommands.getServers().get(server), port);
			
			if(FTPReply.isPositiveCompletion(client.getReplyCode()))
				if(client.login(username, password))
					return client.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			return false;
		}
		
		catch(IOException e)
		{
			return false;
		}
	}
	
	
	/**
	 * Log out and disconnect from the FTP server.
	 * 
	 * @param client The FTP client.
	 * 
	 * @return True if the client could log out and disconnect from the FTP server.
	 */
	public static boolean logoutAndDisconnect(FTPClient client)
	{
		try
		{
			if(!client.logout())
				return false;
			
			client.disconnect();
			
			return true;
		}
		
		catch(IOException e)
		{
			return false;
		}
	}
}
