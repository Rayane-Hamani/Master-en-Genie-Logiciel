package treeftp;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main
{
	/* attributes */
	private static Client client;
	
	private static int recursion;
	private static int recursionRemaining;
	
	/* getters */
	public  static Client getClient() { return client; }
	
	public  static int  getRecursion() { return recursion; }
	public  static int  getRecursionRemaining() { return recursionRemaining; }
	
	/* setters */
	public  static void setRecursionRemaining(int number) { recursionRemaining += number; }
	
	
	
	public static void main(String[] args)
	{
		/* we initialize the starting directory */
		String directory;
		switch(args.length)
		{
			case 0 :
				directory = "/";
				break;
			case 1 :
				directory = "/";
				break;
			default :
				directory = args[1];
				break;
		}
		
		
		
		/* we initialize the number of recursion */
		switch(args.length)
		{
			case 0 :
				recursion = 1;
				break;
			case 1 :
				recursion = 1;
				break;
			case 2 :
				recursion = 1;
				break;
			default :
				recursion = Integer.parseInt(args[2]);;
				break;
		}
		recursionRemaining = recursion;
		
		
		
		try
		{
			client = ClientUtil.createClient(args);
			
			client.connectionToTheServer();
			
			System.out.println(Color.ANSI_BOLD  + Color.ANSI_BLUE_TEXT + directory + Color.ANSI_RESET);
			client.tree(directory);
			
			client.disconnectionToTheServer();
		}
		catch(NoArgsException e)      { System.out.println(e) ;}
		catch(UnknownHostException e) { System.out.println(e) ;}
		catch(IOException e)          { System.out.println(e) ;}
	}
}
