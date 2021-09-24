package ftpserver.command;

import java.io.File;

public class Rnfr
{
	
	/**
	 * Tell which file to rename
	 * 
	 * @param path the path of the working directory
	 * @param filename the path of the file to rename, from the working directory
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String path, File fileToRename, String filename)
	{
		File file = new File(path + filename);
		if(file.isFile() || file.isDirectory())
		{
			fileToRename = file;
			return "350 Please, use RNTO with the new name of the file now.";
		}
		else
		{
			return "550 File doesn't exist.";
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
		return status.startsWith("350");
	}
	
}
