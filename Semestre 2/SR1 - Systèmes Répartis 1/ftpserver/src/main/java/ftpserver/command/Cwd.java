package ftpserver.command;



import java.io.File;
import java.nio.file.Files;

import ftpserver.Main;



public class Cwd
{	
	
	/**
	 * Change the working directory
	 * 
	 * @param curPath the path of the current working directory
	 * @param subPath the path of the new working directory, from the current working directory 
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String curPath, String subPath)
	{
		String path;
		
		/* we form the absolute path                    */
		if(subPath.substring(0, 1).equals("/"))
			path = Main.getServer().getRoot() + subPath.substring(1, subPath.length());
		else
			path = curPath + subPath;
		
		/* we remove the "/" if it's the last character */
		if(path.substring(path.length()-1).equals("/"))
			path = path.substring(0, path.length()-2);
		
		/* we check if the path leads to a directory    */
		/* if so, it is our new path                    */
		/* and we put back the "/" at the end           */
		if(Files.isDirectory(new File(path).toPath()))
		{
			curPath = path + "/";
			return "250 Directory successfully changed.";
		}
		else
			return "550 Failed to change working directory.";
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
		return status.startsWith("250");
	}
	
}
