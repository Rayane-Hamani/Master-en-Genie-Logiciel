package ftpserver.command;

import java.io.File;

public class Rnto
{
	
	/**
	 * Rename a file
	 * 
	 * @param file the file to rename
	 * @param filename the new name of the file
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(File file, String filename)
	{	
		if(!filename.isEmpty())
			if(file.renameTo(new File(filename)))
				return "250 File renamed.";
			else
				return "530 File couldn't be renamed.";
		else
			return "530 Cannot name a file with empty String.";
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
