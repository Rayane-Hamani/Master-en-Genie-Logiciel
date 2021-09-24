package ftpserver.command;

import java.io.File;

public class Mkd
{
	
	/**
	 * Create a new directory
	 * 
	 * @param path the path of the working directory
	 * @param dirname the path of the directory to create, from the working directory
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String path, String dirname)
	{
		if(new File(path + dirname).mkdir())
		{
			return "257 Directory created.";
		}
		else
		{
			return "530 Couldn't create the directory.";
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
