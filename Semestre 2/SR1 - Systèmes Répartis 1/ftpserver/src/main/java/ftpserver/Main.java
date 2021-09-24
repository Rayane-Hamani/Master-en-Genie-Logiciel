package ftpserver;



import ftpserver.account.Account;
import ftpserver.account.AccountUtil;
import ftpserver.server.Server;
import java.io.IOException;
import java.util.Scanner;



public class Main
{
	
	private static Server server;
	public static Server getServer() { return server; }
	
	
	
	public static void main(String[] args)
	{
		/* on remplit la "base de données" de compte pour la démo */
		
		AccountUtil.setAccounts("rayane", new Account("rayane", "hamani"));
		AccountUtil.setAccounts("florian", new Account("florian", "dendoncker"));
		AccountUtil.setAccounts("anonymous", new Account("anonymous", ""));
		
		/* on démarre le serveur */
		
		try
		{
			if(args.length>0)
				server = new Server(Integer.parseInt(args[0]));
			else
				server = new Server(21);
			
			new Thread(server).start();
		}
		
		catch(IOException e) { System.out.println("Failed to open the server."); }
		
		/* on attend l'instruction de l'utilisateur fermer le serveur */
		
		Scanner scanner = new Scanner(System.in);
		while(true)
		{
			if(scanner.next().toLowerCase().equals("stop"))
			{
				scanner.close();
				System.exit(0);
			}
		}
	}
	
}
