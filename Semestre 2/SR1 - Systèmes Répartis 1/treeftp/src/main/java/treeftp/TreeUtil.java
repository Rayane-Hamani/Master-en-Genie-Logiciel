package treeftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.UnknownHostException;

public class TreeUtil
{	
	/**
	 * Return the type of the file from its status
	 * 
	 * @param fileStatus the status of the file
	 * 
	 * @return the type of the file
	 */
	public static String getFiletype(String fileStatus)
	{
		return fileStatus.substring(0, 1);
	}
	
	/**
	 * Return the name of the file and its link if it has one, from its status
	 * 
	 * @param fileStatus the status of the file
	 * @param filetype the type of the file
	 * 
	 * @return the name of the file and its link
	 */
	public static String getFilename(String fileStatus, String filetype)
	{
		String filename;
		
		switch(filetype)
		{
			case "l" :
				String whatIsBeforeTheArrow = fileStatus.substring(0, fileStatus.lastIndexOf(" -> "));
				filename = fileStatus.substring(whatIsBeforeTheArrow.lastIndexOf(" ")+1);
				break;
			default :
				filename = fileStatus.substring(fileStatus.lastIndexOf(" ")+1);
				break;
		}
		
		return filename;
	}

	/**
	 * Add spaces for the indentation
	 * 
	 * @param numberOfIndentation the number of indentation
	 * 
	 * @return the spaces
	 */
	public static String indentation(int numberOfIndentation)
	{
		String spaces = "";
		
		for(int i=0; i<numberOfIndentation; i++)
		{
			spaces += "│    ";
		}
		
		return spaces;
	}

	/**
	 * Read each file in the list in depth and print it with a color which depends of its type
	 * 
	 * @param dataReader the file reader
	 * 
	 * @throws UnknownHostException if an address cannot be resolved during the depth first search
	 * @throws IOException if an IO error occurs during the read of the files
	 */
	public static void printTheList(BufferedReader dataReader) throws IOException
	{
		String file;
		while((file = dataReader.readLine()) != null)
		{
			String filetype = TreeUtil.getFiletype(file);
			String filename = TreeUtil.getFilename(file, filetype);

			System.out.println(indentation(Main.getRecursion() - Main.getRecursionRemaining()) + "├─── " + Color.colorText(filetype, filename) + Color.ANSI_RESET);
			
			depthFirstSearch(filename, filetype);
		}
	}
	
	/**
	 * Verify if a file is a directory and if so, open it to use the tree on it if there is still recursion remaining
	 * 
	 * @param filename the name of the file
	 * @param filetype the type of the file
	 * 
	 * @throws IOException if an IO error occurs during the change in the parent directory
	 */
	public static void depthFirstSearch(String filename, String filetype) throws IOException
	{
		Client client = Main.getClient();
		
		if(filetype.equals("d"))
		{
			if(Main.getRecursionRemaining() > 0)
			{
				Main.setRecursionRemaining(-1);
				if(!(client.getAddress().equals("ftp.free.fr") && filename.equals("stats"))) // The server interrupts the connection when we try to access this directory so this a "quick" fix
				{
					client.tree(filename);
				
					String response = client.send(Command.cwd(".."));
					ClientUtil.ifNotASuccessfulResponse(response, SuccessCode.cwd, Error.cwd);
				}
				Main.setRecursionRemaining(1);
			}
		}		
	}
	
	
}
