package flopbox.command;

import java.util.HashMap;
import java.util.Map;



public class ServerCommands
{
	/* Attributes */
	private static Map<String,String> servers = new HashMap<String,String>();
	
	/* Getters */
	public static Map<String,String> getServers() { return servers; }
	
	
	
	/**
	 * Print the registered servers and their hosts.
	 * 
	 * @return the registered servers and their hosts.
	 */
	public static String print()
	{
		String associations = "";
		
		for(String server : servers.keySet())
		{
			associations += server + " -> " + servers.get(server);
			associations += "\n";
		}
		
		return associations;
	}
	
	
	
	/**
	 * Register a server and its host in the platform.
	 * 
	 * @param server The server.
	 * @param host The host of the server.
	 * 
	 * @return How the command went.
	 */
	public static String register(String server, String host)
	{
		if(servers.containsKey(server))
			return "The server \"" + server + "\" is already registered.";
		
		servers.put(server, host);
		
		return "The association \"" + server + " -> " + host + "\" has been registered.";
	}
	
	
	
	/**
	 * Delete a registered server of the platform.
	 * 
	 * @param server The server.
	 * 
	 * @return How the command went.
	 */
	public static String delete(String server)
	{
		if(servers.remove(server) == null)
			return "The server \"" + server + "\" is not registered.";
		else
			return "The server \"" + server + "\" has been deleted.";
	}
	
	
	
	/**
	 * Modify the host of a server of the platform.
	 * 
	 * @param server The server.
	 * @param host The new host of the server.
	 * 
	 * @return How the command went.
	 */
	public static String modify(String server, String host)
	{
		if(servers.replace(server, host) == null)
			return "The server \"" + server + "\" is not registered.";
		else
			return "The server \"" + server + "\" is now associated with the host \"" + host + "\".";
	}
}
