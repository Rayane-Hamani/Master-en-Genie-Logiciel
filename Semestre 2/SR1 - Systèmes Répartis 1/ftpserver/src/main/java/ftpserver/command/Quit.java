package ftpserver.command;



public class Quit
{
	
	/**
	 * Close all ressources including the passive connection
	 * 
	 * @param connection the current connection of the thread
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(boolean isRunning)
	{
		isRunning = false;
		
		return "221 Goodbye.";
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
		return status.startsWith("221");
	}
	
}
