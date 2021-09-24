package ftpserver.command;

public class Auth
{
	
	/**
	 * To bypass all AUTH tries (to implement in the future)
	 * 
	 * @return that the command has not been implemented
	 */
	public static String execute()
	{
		return "502 Command not implemented.";
	}

}