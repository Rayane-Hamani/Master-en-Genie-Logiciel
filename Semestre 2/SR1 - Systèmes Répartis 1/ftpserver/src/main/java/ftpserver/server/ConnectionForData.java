package ftpserver.server;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class ConnectionForData
{
	
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	
	public Socket getSocket() { return socket; }
	public PrintWriter getWriter() { return writer; } 
	public BufferedReader getReader() { return reader; } 
	
	
	
	public ConnectionForData(Socket socket) throws IOException
	{
		this.socket = socket;
		this.writer = new PrintWriter(socket.getOutputStream(), true);
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	
	
	public void send(String message)
	{
		writer.println(message + "\r\n");
		writer.flush();
	}
	
	/* TO DO things */
	
}
