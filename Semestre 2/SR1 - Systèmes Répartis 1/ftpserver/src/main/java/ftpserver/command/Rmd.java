package ftpserver.command;



import java.io.File;
import java.nio.file.Files;

import ftpserver.Main;



public class Rmd
{
	
	/**
	 * Remove a directory
	 * 
	 * @param curPath the current path of the working directory
	 * @param subPath the path of the directory to remove, from the working directory
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
		
		File directory = new File(path);
		if(Files.isDirectory(directory.toPath()))
			if(directory.delete())
				return "250 Directory removed.";
			else
				return "530 Directory couldn't be deleted.";
		else
			return "550 Directory doesn't exist.";
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
