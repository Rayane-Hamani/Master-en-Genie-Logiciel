package agentflopbox;



import java.io.IOException;

import org.apache.commons.httpclient.HttpMethodBase;



public class CommandUtil
{
	/**
	 * Set the connection headers of a request.
	 * 
	 * @param request The request.
	 * @param connection The connection.
	 */
	public static void setConnectionHeaders(HttpMethodBase request, Server connection)
	{
		request.setRequestHeader( "port"     , connection.getPort()               );
		request.setRequestHeader( "username" , connection.getUser().getUsername() );
		request.setRequestHeader( "password" , connection.getUser().getPassword() );
	}
	
	/**
	 * Send a request to execute to the client.
	 * 
	 * @param request The request to execute.
	 * 
	 * @return If the request has been sent.
	 */
	public static boolean send(HttpMethodBase request)
	{
		try
		{
			Main.agent.getClient().executeMethod(request);
			return true;
		}
		
		catch(IOException e)
		{
			return false;
		}
	}
	
	/**
	 * Receive the response to a request.
	 * 
	 * @param request The request.
	 * 
	 * @return The response.
	 */
	public static String receive(HttpMethodBase request)
	{
		try
		{
			return request.getResponseBodyAsString();
		}
		
		catch(IOException e)
		{
			return "The response couldn't be received.";
		}
	}
}
