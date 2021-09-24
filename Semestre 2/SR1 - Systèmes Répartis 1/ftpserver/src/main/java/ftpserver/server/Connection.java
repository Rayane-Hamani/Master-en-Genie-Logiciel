package ftpserver.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ftpserver.Main;
import ftpserver.command.*;

public class Connection implements Runnable
{

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	private String username;
	private boolean isLogged = false;
	
	private Type type;
	
	private boolean isInPassiveOrActiveMode;
	private ConnectionForData connectionForData;
	
	private boolean isRunning;
	
	private String path;
	private File fileToRename;
	
	

	public Connection(Socket socket)
	{
		try
		{
			this.socket = socket;
			this.writer = new PrintWriter(socket.getOutputStream(), true);
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			this.username = ""; 
			this.isLogged = false;
			
			this.type = Type.BINARY;
			
			this.isInPassiveOrActiveMode = false;
			this.connectionForData = null;
			
			this.isRunning = true;
			
			this.path = Main.getServer().getRoot();
			this.fileToRename = null;
		}
		
		catch(IOException e)
		{
			this.isRunning = false;
		}
	}
	
	
	
	/**
	 * Run the connection
	 * Ask to login first before letting the user execute other commands
	 */
	public void run()
	{
		String response;

		response = "220 Service ready for a new user.";
		send(response);

		/* On force la connexion */

		while (isRunning && !isLogged)
		{
			response = login();
			send(response);
		}

		/* On exécute les commandes de l'utilisateur */

		while (isRunning && isLogged)
		{
			response = execute();
			send(response);
		}
		
		/* on ferme les ressources liées à cette connexion */
		
		closeResources();
	}
	
	
	
	/**
	 * Send a message to the client
	 * 
	 * @param message the message to send
	 */
	public void send(String message)
	{
		writer.println(message + "\r\n");
		writer.flush();
	}
	
	
	
	/**
	 * Receive and execute the only acceptable commands when a user is not logged
	 * yet. The only acceptable commands are AUTH, USER, PASS and QUIT. They should
	 * be enough for the user to log in
	 * 
	 * @return the status of how the command went
	 */
	public String login()
	{
		String response;

		try
		{
			String[] command = reader.readLine().split(" ");

			switch(command[0].toUpperCase())
			{
			case "AUTH":
				response = Auth.execute();
				break;
			case "USER":
				response = User.execute(command[1]);
				if (User.isSuccessful(response))
					this.username = command[1];
				break;
			case "PASS":
				response = Pass.execute(this.username, command[1]);
				if(Pass.isSuccessful(response))
					this.isLogged = true;
				else
					this.isRunning = false;
				break;
			case "QUIT":
				response = Quit.execute(this.isRunning);
				break;
			default:
				response = "530 Please login with USER and PASS.";
				break;
			}
		}

		catch(IOException e)
		{
			response = "500 The read of the command failed. Try again please.";
		}
		catch(NullPointerException e)
		{
			response = Quit.execute(this.isRunning);
		}

		return response;
	}

	
	
	public String execute()
	{
		String response;

		try
		{
			String[] command = reader.readLine().split(" ");

			switch(command[0].toUpperCase())
			{
			case "AUTH":
				response = Auth.execute();
				break;
			case "USER":
				response = "530 Can't change once logged in.";
				break;
			case "PASS":
				response = "530 Can't change once logged in.";
				break;
			case "PWD":
				response = Pwd.execute(this.path);
				break;
			case "CWD":
				response = Cwd.execute(this.path, command[1]);
				break;
			case "TYPE":
				response = ftpserver.command.Type.execute(this.type, command[1]);
				break;
			case "PASV":
				 response = Pasv.execute(this.connectionForData);
				 break;
			case "PORT":
				 response = Port.execute(this.connectionForData, command[1]);
				 break;
			case "LIST":
				response = List.execute(this.path);
				if (List.isSuccessful(response))
				{
					this.connectionForData.send(response.substring(response.indexOf("\n") + 1));
					response = response.substring(0, response.indexOf("\n") + 0);
				}
				break;
			case "STOR":
				response = Stor.execute(this.connectionForData.getReader(), this.path, command[1]);
				break;
			case "DELE":
				response = Dele.execute(this.path, command[1]);
				break;
			case "MKD":
				response = Mkd.execute(this.path, command[1]);
				break;
			case "RMD":
				response = Rmd.execute(this.path, command[1]);
				break;
			case "RNFR":
				response = Rnfr.execute(this.path, this.fileToRename, command[1]);
				break;
			case "RNTO":
				response = Rnto.execute(this.fileToRename, command[1]);
				break;
			case "QUIT":
				response = Quit.execute(this.isRunning);
				System.out.println("LALALA");
				break;
			default:
				response = "500 Unknown command.";
				break;
			}
		}

		catch(IOException e)
		{
			response = "500 The read of the command failed. Try again please.";
		}
		catch(NullPointerException e)
		{
			response = Quit.execute(this.isRunning);
		}

		return response;
	}
	
	
	
	/**
	 * Close all resources of this connection
	 */
	public void closeResources()
	{	
		try
		{
			writer.close();
			reader.close();
			socket.close();
			
			if(isInPassiveOrActiveMode)
			{
				connectionForData.getWriter().close();
				connectionForData.getReader().close();
				connectionForData.getSocket().close();
			}
		}
		
		catch(IOException e)
		{
			// If an IOException happens during the close of the readers or the sockets, we close them quietly
			// IOUtils.closeQuietly(reader);
			// IOUtils.closeQuietly(socket);
			// if(isInPassiveMode)
			// {
			//     IOUtils.closeQuietly(passiveConnection.getReader());
			//     IOUtils.closeQuietly(passiveConnection.getSocket());
			// }
		}
	}

}
