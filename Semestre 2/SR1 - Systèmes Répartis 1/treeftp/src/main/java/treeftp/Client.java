package treeftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client
{
	private String address;
	private int port;
	
	private String username;
	private String password;
	
	private Socket commandSocket;
	private PrintWriter commandWriter;
	private BufferedReader commandReader;
	
	
	public Client(String address, String username, String password)
	{
		this.address            = address   ;
		this.port               = 21;
		
		this.username           = username  ;
		this.password           = password  ;
	}
	
	
	
	/* getters */
	public String getAddress() { return address; }
	
	
	
	/**
	 * Send a command and return the response received
	 * 
	 * @param command the command sent
	 * @return the response received
	 * 
	 * @throws IOException if an IO error occurs during the read
	 */
	public String send(String command) throws IOException
	{
		commandWriter.println(command);
		
		String response;
		
		if(command.equals(Command.list()))
			response = commandReader.readLine() + " " + commandReader.readLine();
		else
			response = commandReader.readLine();
			
		return response;
	}
	
	/**
	 * Connect to the server with the username and the password and initialize the command communication channels
	 * 
	 * @throws UnknownHostException if the address cannot be resolved
	 * @throws IOException if an IO error occurs during the connection
	 */
	public void connectionToTheServer() throws UnknownHostException, IOException
	{
		commandSocket = new Socket(address, port);
		
		commandWriter = new PrintWriter(commandSocket.getOutputStream(), true);
		commandReader = new BufferedReader(new InputStreamReader(commandSocket.getInputStream()));
		
		String response;
		
		response = commandReader.readLine();
		ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.waitingForEntry, Error.waitingForEntry);
		
		response = send(Command.user(username));
		ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.user, Error.user);
		
		response = send(Command.pass(password));
		ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.pass, Error.pass);
	}
	
	/**
	 * Connect to the server in passive mode for data transmission
	 * 
	 * @return the freshly opened socket of the connection in passive mode
	 * 
	 * @throws UnknownHostException if the address cannot be resolved
	 * @throws IOException if an IO error occurs during the connection in passive mode
	 */
	public Socket connectionInPassiveMode() throws UnknownHostException, IOException
	{
		String response = send(Command.pasv());
		ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.pasv, Error.pasv);

		String[] responseSplit = ClientUtil.getAddressAndPort(response);
		
		return new Socket(ClientUtil.getAddress(responseSplit), ClientUtil.getPort(responseSplit));
	}	
	
	/**
	 * Open the directory and a connection to list its files
	 * 
	 * @param directory the directory to open
	 * 
	 * @throws UnknownHostException if the address while connecting in passive mode cannot be resolved
	 * @throws IOException if an IO error occurs 
	 */
	public void tree(String directory) throws UnknownHostException, IOException
	{
		String response;
		
		response = send(Command.cwd(directory));
		
		/* We skip the directories that we fail to open and notice the user in the console */
		if(!response.startsWith(SuccessCode.cwd))
		{
			System.out.println(response);
			return;
		}
		
		Socket dataSocket = connectionInPassiveMode();
		
		response = send(Command.list());
		ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.list, Error.list);

		TreeUtil.printTheList(new BufferedReader(new InputStreamReader(dataSocket.getInputStream())));
		
		dataSocket.close();
	}
	
	/**
	 * Disconnect to the server and close the streams and socket
	 * 
	 * @throws IOException if an IO error occurs while closing the socket
	 */
	public void disconnectionToTheServer() throws IOException
	{
		String response = send(Command.quit());
		ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.quit, Error.quit);

		commandReader.close();
		commandWriter.close();
		commandSocket.close();
	}
}
