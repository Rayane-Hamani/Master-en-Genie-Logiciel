package ftpserver.command;



import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import ftpserver.server.ConnectionForData;



public class Pasv
{
	
	/**
	 * Prepare the address and the port to communicate the data with to send
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(ConnectionForData connectionForData)
	{
		try
		{
			if(connectionForData != null)
				return "530 Already in passive mode.";
			
			ServerSocket serverSocket = new ServerSocket(0);
			int port = serverSocket.getLocalPort();
			serverSocket.close();
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			String address = inetAddress.toString().replace(".", ",");
			int p1         = (int) port/256;
			int p2         = (int) port%256;
			
			connectionForData = new ConnectionForData(new Socket(inetAddress, port));
						
			return "227 Entering Passive Mode (" + address  + ","
												 + p1       + ","
												 + p2       + ").";
		}
		
		catch(IOException e)
		{
			return "530 An error occured while entering passive mode.";
		}
	}
	
	/**
	 * Return if the status of the command tells the command ran successfully
	 * 
	 * @param status the status of the command
	 * 
	 * @return if the command ran successfully
	 */
	public static boolean isSuccessful(String status)
	{
		return status.startsWith("227");
	}
	
}
