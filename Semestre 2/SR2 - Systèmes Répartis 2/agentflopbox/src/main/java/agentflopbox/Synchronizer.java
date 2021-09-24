package agentflopbox;



import java.io.File;
import java.io.IOException;
import java.util.Date;



public class Synchronizer
{
	private String root, bin;
	
	
	
	public Synchronizer()
	{
		root = Main.agent.getRootPath();
		bin  = Main.agent.getBinPath();
	}
	
	
	/**
	 * Check if the date of a local file is newer, older or identical to its remote equivalent.
	 * 
	 * @param localDate The date of the local file.
	 * @param remoteDate The date of the remote equivalent.
	 * 
	 * @return If the local file is newer, older or identical.
	 */
	public String checkTimeOfLastModification(Date localDate, Date remoteDate)
	{
		if(localDate.after(remoteDate))
			return "Newer";
					
		if(remoteDate.after(localDate))
			return "Older";
		
		return "Same";
	}
	
	
	
	public void check(Server server)
	{
		
	}
}
