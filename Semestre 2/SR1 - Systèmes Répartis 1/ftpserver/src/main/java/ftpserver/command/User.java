package ftpserver.command;

import ftpserver.account.AccountUtil;

public class User
{

	/**
	 * Check if the username if registered
	 * 
	 * @param username the username to try
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String username)
	{
		if(AccountUtil.isRegistered(username))
			return "331 Please specify the password.";
		else
			return "530 User not registered. This server can be accessed by anonymous";
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
		return status.startsWith("331");
	}
	
}
