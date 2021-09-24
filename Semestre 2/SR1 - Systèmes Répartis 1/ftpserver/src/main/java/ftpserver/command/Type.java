package ftpserver.command;



public class Type
{
	
	/**
	 * Switch the mode
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(ftpserver.server.Type type, String typeToSet)
	{		
		String response;
		switch(typeToSet)
		{
		case "A": // Non-print
			type = ftpserver.server.Type.ASCII;
			response = "200 Switching to Binary mode.";
			break;
		case "I": // Binary
			type = ftpserver.server.Type.BINARY;
			response = "200 Switching to Binary mode.";
			break;
		default :
			response = "530 Failed to change the mode.";
			break;
		}
		
		return response;
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
		return status.startsWith("200");
	}
	
}
