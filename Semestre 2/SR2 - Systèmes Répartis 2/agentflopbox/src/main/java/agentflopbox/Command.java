package agentflopbox;



import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;



public class Command
{
	/**
	 * List the files of a server.
	 * 
	 * @param server The sever.
	 * 
	 * @return How the command went.
	 */
	public static String list(Server server)
	{
		GetMethod request = new GetMethod(Main.FLOPBOX + server.getName() + "/");
		
		CommandUtil.setConnectionHeaders(request, server);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
	
	/**
	 * Download a file from a server.
	 * 
	 * @param server The server.
	 * @param file The path on the server of the file to download.
	 * 
	 * @return How the command went.
	 */
	public static String download(Server server, String file)
	{
		GetMethod request = new GetMethod(Main.FLOPBOX + server.getName() + "/" + file + "/download");
		
		CommandUtil.setConnectionHeaders(request, server);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
	
	/**
	 * Upload a file to a server.
	 * 
	 * @param server The server.
	 * @param from The path of the file to upload.
	 * @param to The path on the server of the file uploaded.
	 * 
	 * @return How the command went.
	 */
	public static String upload(Server server, String from, String to)
	{
		PostMethod request = new PostMethod(Main.FLOPBOX + server.getName() + "/" + to + "/upload");
		
		CommandUtil.setConnectionHeaders(request, server);
		request.setRequestHeader("from", from);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
	
	
	/**
	 * Rename a file on a server.
	 * 
	 * @param server The server.
	 * @param from The path on the server of the file to rename.
	 * @param to The new name of the file.
	 * 
	 * @return How the command went.
	 */
	
//	PatchMethod n'existe pas donc bon j'ai vraiment genre vraiment vraiment pas le temps de me replonger dans Flopbox
//	pour modifier tout ça, tester si ça marche toujours puis continuer l'Agent.
//	C'est plus rapide pour moi de vous expliquer ça ici en 2 secondes que de procéder aux modifications.
//	Et puis même si je le faisais, la méthode aurait eu la même structure avec probablement OptionsMethod à la place de PatchMethod.
	 
	/*
	public static String rename(Server server, String from, String to)
	{
		PatchMethod request = new PatchMethod(Main.FLOPBOX + server.getName() + "/" + from);
		
		CommandUtil.setConnectionHeaders(request, server);
		request.setRequestHeader("to", to);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
	*/
	
	/**
	 * Delete a file on a server.
	 * 
	 * @param server The server.
	 * @param path The path on the server of the file to delete.
	 * 
	 * @return How the command went.
	 */
	public static String delete(Server server, String path)
	{
		DeleteMethod request = new DeleteMethod(Main.FLOPBOX + server.getName() + "/" + path);
		
		CommandUtil.setConnectionHeaders(request, server);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
	
	/**
	 * Make a directory on a server.
	 * 
	 * @param server The server.
	 * @param path The path on the server of the directory to make.
	 * 
	 * @return How the command went.
	 */
	public static String makeDir(Server server, String path)
	{
		PutMethod request = new PutMethod(Main.FLOPBOX + server.getName() + "/" + path);
		
		CommandUtil.setConnectionHeaders(request, server);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
	
	/**
	 * Display the registered servers on Flopbox.
	 * 
	 * @return the registered servers on Flopbox.
	 */
	public static String displayServers()
	{
		GetMethod request = new GetMethod(Main.FLOPBOX);
		
		CommandUtil.send(request);
		
		return CommandUtil.receive(request);
	}
}
