package ftpserver.command;



import java.io.IOException;
import java.util.Scanner;



public class List
{
	
	/**
	 * List the file of the working directory
	 * 
	 * @param path the path of the working directory
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(String path)
	{
		String response;
		
		try
		{
			Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", "ls -l" + path});
			p.waitFor();
			
			response = "226 Directory sent OK.\n";
			Scanner scanner = new Scanner(p.getInputStream()).useDelimiter("\n");
			scanner.next(); // to remove the line with total
			while(scanner.hasNext())
			{
				response += scanner.next() + "\n";
			}
			scanner.close();
			p.destroy();
		}
		
		catch(IOException | InterruptedException e)
		{
			response = "550 Failed to list.";
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
		return status.startsWith("226");
	}
	
}
