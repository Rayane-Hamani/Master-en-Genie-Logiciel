package agentflopbox;

import java.io.File;
import java.io.IOException;

public class Server
{
	private String name;
	private String port;
	private User   user;
	private String path;
	
	
	
	public Server(String name, String port, User user)
	{
		this.name = name;
		this.port = port;
		this.user = user;
		
		createLocalDirectory();
	}
	
	public Server(String name, String port)
	{
		this.name = name;
		this.port = port;
		this.user = new User("anonymous", "anonymous");
		
		createLocalDirectory();
	}
	
	public Server(String name)
	{
		this.name = name;
		this.port = "21";
		this.user = new User("anonymous", "anonymous");
		
		createLocalDirectory();
	}
	
	
	
	private void createLocalDirectory()
	{
		this.path = Main.agent.getRootPath() + this.name;
		
		File dir = new File(this.path);
		
		try
		{
			this.path = dir.getCanonicalPath();
		}
		catch (IOException e)
		{
			/* do nothing */
		}
		
		if(!dir.exists())
			dir.mkdirs();
	}
	
	
	
	public String getName() { return this.name; }
	public String getPort() { return this.port; }
	public User   getUser() { return this.user; }

	public void setPort(String port) { this.port = port; }
	public void setUser(User user)   { this.user = user; }
}
