package flopbox;

import flopbox.client.ClientUtil;
import flopbox.command.*;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.net.ftp.FTPClient;



/**
 * Root resource (exposed at "/" path)
 */
@Path("/")
public class MyResource
{
	/**
	 * Print the registered servers and their hosts.
	 * 
	 * @return the registered servers and their hosts.
	 */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String printServers()
    {
        return ServerCommands.print();
    }
    
    /**
	 * Register a server and its host in the platform.
	 * 
	 * @param server The server.
	 * @param host The host of the server.
	 * 
	 * @return How the command went.
	 */
    @PUT
    @Path("{server}")
    @Produces(MediaType.TEXT_PLAIN)
    public String registerServer(@PathParam("server") String server, @HeaderParam("host") String host)
    {
    	return ServerCommands.register(server, host) + "\n";
    }
    
    /**
	 * Delete a registered server of the platform.
	 * 
	 * @param server The server.
	 * 
	 * @return How the command went.
	 */
    @DELETE
    @Path("{server}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteServer(@PathParam("server") String server)
    {
    	return ServerCommands.delete(server) + "\n";
    }
    
    /**
	 * Modify the host of a server of the platform.
	 * 
	 * @param server The server.
	 * @param host The new host of the server.
	 * 
	 * @return How the command went.
	 */
    @PATCH
    @Path("{server}")
    @Produces(MediaType.TEXT_PLAIN)
    public String modifyServer(@PathParam("server") String server, @HeaderParam("host") String host)
    {
    	return ServerCommands.modify(server, host) + "\n";
    }
    
    
    
    /**
     * Create a directory on the FTP server.
     * 
     * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
     * @param path The path of the file directory to create.
     * 
     * @return How the command went.
     */
    @PUT
    @Path("{server}/{path:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    public String makeDir(@PathParam  ("server"  ) String server  ,   @HeaderParam("port"    ) int    port    ,
    					  @HeaderParam("username") String username,   @HeaderParam("password") String password,
    					  @PathParam  ("path"    ) String path    )
    {
    	FTPClient client = new FTPClient();
    	
    	String response;
    	
    	if(ClientUtil.connectAndLogin(client, server, port, username, password))
    	{
    		response = FtpCommands.makeDir(client, path);
    		ClientUtil.logoutAndDisconnect(client);
    	}
    	else
    		return "The connection to the server failed.";
    	
    	return response + "\n";
    }
    
    /**
     * Delete a file or directory on the FTP server.
     * 
     * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
     * @param path The path of the file or directory to delete.
     * 
     * @return How the command went.
     */
    @DELETE
    @Path("{server}/{path:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    public String delete(@PathParam  ("server"  ) String server  ,   @HeaderParam("port"    ) int    port    ,
			   			 @HeaderParam("username") String username,   @HeaderParam("password") String password,
			   			 @PathParam  ("path"    ) String path    )
    {
    	FTPClient client = new FTPClient();
    	
    	String response;
    	
    	if(ClientUtil.connectAndLogin(client, server, port, username, password))
    	{
    		response = FtpCommands.delete(client, path);
    		ClientUtil.logoutAndDisconnect(client);
    	}
    	else
    		return "The connection to the server failed.";
    	
    	return response + "\n";
    }
    
    /**
     * Download a file.
     * 
     * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
     * @param path The path of the file to download.
     * 
     * @return How the command went.
     */
    @GET
    @Path("{server}/{path:.*}/download")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.TEXT_PLAIN})
    public String download(@PathParam  ("server"  ) String server   ,   @HeaderParam("port"    ) int    port    ,
    					   @HeaderParam("username") String username ,   @HeaderParam("password") String password,
    					   @PathParam  ("path"    ) String path     )
    {
    	FTPClient client = new FTPClient();
    	
    	String response;
    	
    	if(ClientUtil.connectAndLogin(client, server, port, username, password))
    	{
    		response = FtpCommands.download(client, path);
    		ClientUtil.logoutAndDisconnect(client);
    	}
    	else
    		response = "The connection to the server failed.";
    	
    	return response + "\n";
    }
    
    /**
     * List information about a file or files in a directory on the FTP server.
     * 
     * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
     * @param path The path of the file or directory.
     * 
     * @return Information about the file or files in the directory.
     */
    @GET
    @Path("{server}/{path:.*}")
    @Produces(MediaType.TEXT_PLAIN)
    public String list(@PathParam  ("server"  ) String server  ,   @HeaderParam("port"    ) int    port    ,
    				   @HeaderParam("username") String username,   @HeaderParam("password") String password,
    				   @PathParam  ("path"    ) String path    )
    {
    	FTPClient client = new FTPClient();
    	
    	String response;
    	
    	if(ClientUtil.connectAndLogin(client, server, port, username, password))
    	{
    		response = FtpCommands.list(client, path);
    		ClientUtil.logoutAndDisconnect(client);
    	}
    	else
    		response = "The connection to the server failed.";
    	
    	return response;
    }
    
    /**
     * Rename a file or directory on the FTP server.
     * 
     * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
     * @param from The path of the file or directory to rename.
     * @param to The new name of the file or directory.
     * 
     * @return How the command went.
     */
    @PATCH
    @Path("{server}/{from}")
    @Produces(MediaType.TEXT_PLAIN)
    public String rename(@PathParam  ("server"  ) String server  ,   @HeaderParam("port"    ) int    port    ,
			 			 @HeaderParam("username") String username,   @HeaderParam("password") String password,
    					 @PathParam  ("from"    ) String from    ,   @HeaderParam("to"      ) String to      )
    {
    	FTPClient client = new FTPClient();
    	
    	String response;
    	
    	if(ClientUtil.connectAndLogin(client, server, port, username, password))
    	{
    		response = FtpCommands.rename(client, from, to);
    		ClientUtil.logoutAndDisconnect(client);
    	}
    	else
    		response = "The connection to the server failed.";
    	
    	return response + "\n";
    }
    
    /**
     * Store a file or directory on the FTP server.
     * 
     * @param server The FTP server to connect to.
     * @param port The port of the FTP server to connect to.
     * @param username The username of the account to use.
     * @param password The password of the account to use.
     * @param from The input stream of the file or directory to upload.
     * @param to The path where to store the input stream.
     * 
     * @return How the command went.
     */
    @POST
    @Path("{server}/{to:.*}/upload")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.TEXT_PLAIN})
    public String upload(@PathParam  ("server"  ) String server   ,   @HeaderParam("port"    ) int    port    ,
    					 @HeaderParam("username") String username ,   @HeaderParam("password") String password,
    					 @HeaderParam("from"    ) String from     ,   @PathParam  ("to"      ) String to      )
    {
    	FTPClient client = new FTPClient();
    	
    	String response;
    	
    	if(ClientUtil.connectAndLogin(client, server, port, username, password))
    	{
    		response = FtpCommands.upload(client, from, to);
    		ClientUtil.logoutAndDisconnect(client);
    	}
    	else
    		response = "The connection to the server failed.";
    	
    	return response + "\n";
    }
}
