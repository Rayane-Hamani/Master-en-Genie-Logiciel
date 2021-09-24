package ftpserver.server;



import java.io.IOException;
import java.net.ServerSocket;



public class Server implements Runnable
{
	
	private ServerSocket serverSocket;			// our server
	
	private final String root = "~/";			// the root of our server (cannot go under /home/user/)
	public String getRoot() { return root; }	// the getter of our root
	
	
	
	public Server(int port) throws IOException
	{
		try
		{
			this.serverSocket = new ServerSocket(port);
		}
		
		catch(IOException e)
		{
			throw new IOException();
		}
	}
	
	
	/**
	 * Accept connections (multi-threaded)
	 */
	public void run()
	{
		while(true)
		{
			try
			{
				new Thread(new Connection(serverSocket.accept())).start();
				System.out.println("Connexion établie !");
			}
			
			catch(IOException e)
			{
				// nothing, we can to continue accepting connections
			}
		}
	}
	
}
