package agentflopbox;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.GetMethod;



public class AgentUtil
{
	/**
	 * Find a registered server by its name.
	 * 
	 * @param name The name of the server to find.
	 * 
	 * @return The server having this name.
	 */
	public static Server findServerByName(String name)
	{
		for(Server server : Main.agent.getServers())
		{
			if(server.getName().equals(name))
				return server;
		}
		
		return null;
	}
	
	

	/**
	 * Load the servers registered on Flopbox in the agent.
	 * 
	 * @param servers The list to fill with all the servers registered on Flopbox.
	 * 
	 * @return If the list has correctly been filled.
	 */
	public static void loadServers(ArrayList<Server> servers)
	{
		String registeredServers = Command.displayServers();
		
		Scanner scanner = new Scanner(registeredServers);
		
		while(scanner.hasNextLine())
		{
			String[] line = scanner.nextLine().split(" -> ");

			String name = line[0].toLowerCase();
			
			servers.add(new Server(name));
		}
		
		scanner.close();
	}
	
	/**
	 * Check if a port is valid.
	 * 
	 * @param portToTest The port to test.
	 * 
	 * @return If the port is valid.
	 */
	public static boolean isPortValid(String portToTest)
	{
		int port;
		
		try                            { port = Integer.parseInt(portToTest); }
		catch(NumberFormatException e) { return false;                        }
		
		return (0 > port || port > 65535);
	}
	
	/**
	 * Load the port and user to use for the servers specified in the config file.
	 * 
	 * @param servers The list of servers registered on Flopbox.
	 */
	public static void loadPortsAndUsers(ArrayList<Server> servers)
	{
		File config = new File("config.ini");
		
		try
		{
			Scanner scanner = new Scanner(config);
			
			while(scanner.hasNextLine())
			{
				String[] line = scanner.nextLine().split(" ");
				
				String name     = line[0].toLowerCase();
				String port     = line[1];
				String username = line[2];
				String password = line[3];
				
				Server server = findServerByName(name);
				
				if(server != null)
				{
					server.setPort(port);
					server.setUser(new User(username, password));
				}
			}
			
			scanner.close();
		}
		
		catch(FileNotFoundException e)
		{
			// do nothing
		}
	}
	

}
