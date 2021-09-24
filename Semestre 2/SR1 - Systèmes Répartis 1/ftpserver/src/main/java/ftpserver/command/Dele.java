package ftpserver.command;

import java.io.File;

public class Dele
{
	
	/**
	 * Delete a file
	 * 
	 * @param path the path of the working directory
	 * @param filename the path of the file to delete, from the working directory
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String path, String filename)
	{
		File file = new File(path + filename);
		if(file.isFile())
		{
			if(file.delete())
				return "250 File deleted.";
			else
				return "530 Couldn't delete the file.";
		}
		else
		{
			return "530 Couldn't delete the file.";
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
		return status.startsWith("257");
	}
	
}
