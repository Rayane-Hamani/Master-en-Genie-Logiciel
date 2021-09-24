package treeftp;

import java.io.IOException;

public class ClientUtil
{
	/**
	 * Create the right client according to the arguments the user used
	 * 
	 * @param args the arguments the user used
	 * 
	 * @return the right client
	 * 
	 * @throws NoArgsException if the user used the program without arguments
	 */
	public static Client createClient(String[] args) throws NoArgsException
	{
		Client client;
		
		switch(args.length)
		{
			case 0 :
				throw new NoArgsException(Error.usage);
			case 1 :
				client = new Client(args[0], "anonymous", "");
				break;
			case 2 :
				client = new Client(args[0], "anonymous", "");
				break;
			case 3 :
				client = new Client(args[0], "anonymous", "");
				break;
			case 4 :
				client = new Client(args[0], args[3], "");
				break;
			default :
				client = new Client(args[0], args[3], args[4]);
				break;
		}
		
		return client;
	}
	
	/**
	 * Return the IPv4 address and the port in the response sent by the ftp server after successfully entering the passive mode
	 * 
	 * @param response the positive response sent by the ftp server
	 * 
	 * @return an array containing the IPv4 address and the port in the positive response sent by the ftp server
	 */
	public static String[] getAddressAndPort(String response)
	{
		String whatIsInsideTheParenthesis = response.substring(response.indexOf("(")+1, response.indexOf(")"));
		return whatIsInsideTheParenthesis.split(",");
	}
	
	/**
	 * Return the IPv4 address in the response sent by the ftp server after successfully entering the passive mode
	 * 
	 * @param response the positive response sent by the ftp server
	 * 
	 * @return the IPv4 address in the positive response sent by the ftp server
	 */
	public static String getAddress(String[] response)
	{
		return response[0] + "." + response[1] + "." + response[2] + "." + response[3];
	}
	
	/**
	 * Return the port in the response sent by the ftp server after successfully entering the passive mode
	 * 
	 * @param response the positive response sent by the ftp server
	 * 
	 * @return the port in the positive response sent by the ftp server
	 */
	public static int getPort(String[] response)
	{
		return Integer.parseInt(response[4])*256 + Integer.parseInt(response[5]);
	}

	/**
	 * Throw an IOException if the response is not positive
	 * 
	 * @param response the response sent by the ftp server
	 * @param code the code the response should start with
	 * @param errorIntroduction the message which introduces the error
	 * 
	 * @throws IOException if the response doesn't start with the code
	 */
	public static void ifNotASuccessfulResponse(String response, String successCode, String errorIntroduction) throws IOException
	{
		if(!response.startsWith(successCode))
			throw new IOException(errorIntroduction + response);
	}
}
