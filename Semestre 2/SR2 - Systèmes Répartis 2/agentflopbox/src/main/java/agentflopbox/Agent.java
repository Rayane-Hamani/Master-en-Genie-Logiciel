package agentflopbox;



import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.httpclient.HttpClient;



public class Agent
{
	private HttpClient client = new HttpClient();
	
	private String rootPath = "~/root/";
	private String binPath  = "~/.deleted/";
	
	public String getRootPath() { return this.rootPath; }
	public String getBinPath()  { return this.binPath; }
	
	private ArrayList<Server> servers = new ArrayList<Server>();
	
	public HttpClient        getClient()    { return this.client; }
	public ArrayList<Server> getServers()   { return this.servers; }
	
	
	
	public Agent()
	{}
	
	
	
	/**
	 * Create the directories for each registered server.
	 */
	public void createDirectories()
	{
		for(Server server : this.servers)
		{
			File dir = new File(this.rootPath + server.getName());
			
			if(!dir.exists())
				dir.mkdirs();
			
		}
	}
	
	/**
	 * Create the bin directory.
	 */
	public void createBin()
	{
		File bin = new File(this.binPath);
		
		if(!bin.exists())
			bin.mkdirs();
	}
	
	/**
	 * Import the data of the registered servers.
	 */
	public void importRemoteData()
	{
		for(Server server : this.servers)
		{
			Scanner scanner = new Scanner(Command.list(server));
			
			while(scanner.hasNext())
			{
				String file = scanner.nextLine();
				
				file = file.substring(file.lastIndexOf(" ")+1);
				
				Command.download(server, file);
			}
			
			scanner.close();
		}
	}
	
	/**
	 * Watch the local files and update the corresponding server when a local change occurs.
	 */
	public void updateServer()
	{
		try
		{
			WatchService watchService = FileSystems.getDefault().newWatchService();
			
			Path path = Paths.get("root");
			
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
			
			WatchKey watchKey;
			while((watchKey = watchService.take()) != null)
			{
				for(WatchEvent<?> event : watchKey.pollEvents())
				{
					String fileCreated = event.context().toString();
					
					Server server = AgentUtil.findServerByName(fileCreated.substring(0, fileCreated.indexOf("/")));
					
					String to = fileCreated.substring(fileCreated.indexOf("/")+1);
					
					if(event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE) || event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY))
						Command.delete(server, to);
					
					if(event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE) || event.kind().equals(StandardWatchEventKinds.ENTRY_MODIFY))
						Command.upload(server, rootPath + "/" + fileCreated, to);
				}
			}
		}
		catch(IOException | InterruptedException e)
		{
			// do nothing
		}
	}
}
