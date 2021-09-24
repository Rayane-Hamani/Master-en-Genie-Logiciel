package ftpserver.command;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import ftpserver.server.ConnectionForData;

public class Port
{
	
	/**
	 * Connect to the address and port received for active connection
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(ConnectionForData connectionForData, String chaine)
	{
		String[] chaineSplit = chaine.split(",");
		
		String address = chaineSplit[0] + "." +
						 chaineSplit[1] + "." +
						 chaineSplit[2] + "." +
						 chaineSplit[3] + "." ;
		
		int port = Integer.parseInt(chaineSplit[4])*256 + Integer.parseInt(chaineSplit[5]);
		
		try
		{
			connectionForData = new ConnectionForData(new Socket(address, port));
		}
		catch(UnknownHostException e)
		{
			return "530 An error occured while entering active mode.";
		}
		catch(IOException e)
		{
			return "530 An error occured while entering active mode.";
		}
		
		return "220 Okay.";
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
		return status.startsWith("200");
	}
	
}
