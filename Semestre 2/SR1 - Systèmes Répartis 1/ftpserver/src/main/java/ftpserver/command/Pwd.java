package ftpserver.command;



public class Pwd
{
	
	/**
	 * Show the current working directory
	 * 
	 * @param path the path of the current working directory
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String path)
	{
		return "257 \"" + path + "\" is the current directory.";
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
