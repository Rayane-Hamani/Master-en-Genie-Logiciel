package ftpserver.command;

import ftpserver.account.AccountUtil;

public class Pass
{
	
	/**
	 * Check if the password of the username is right
	 * 
	 * @param username the username
	 * @param password the password to try
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String username, String password)
	{
		if(username == null || username.trim().isEmpty())
			return "332 User needed.";
			
		if(username.equals("anonymous"))
			return "230 Login successful.";
		else
			if(AccountUtil.getAccounts().get(username).getPassword().equals(password))
				return "230 Login successful.";
			else
				return "530 Login failed.";
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
		return status.startsWith("230");
	}

}
