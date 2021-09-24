package flopbox.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;



public class FtpCommands
{
	/**
	 * Create a directory on the FTP server.
	 * 
	 * @param client The FTP client.
	 * @param path The path of the directory to create.
	 * 
	 * @return How the command went.
	 */
	public static String makeDir(FTPClient client, String path)
	{
		String response;
		
		try
		{
			if(client.makeDirectory(path))
				response = "The directory \"" + path + "\" has been created.";
			else
				response = "The directory \"" + path + "\" could not be created.";
		}
		
		catch(IOException e)
		{
			response = "An IO error occured while sending the command or receiving its result.";
		}
		
		return response;
	}

	
	
	/**
	 * Delete a file or directory on the FTP server.
	 * 
	 * @param client The FTP client.
	 * @param path The path of the file or directory to delete.
	 * 
	 * @return How the command went.
	 */
	public static String delete(FTPClient client, String path)
	{
		String response;
		
		try
		{
			if(client.deleteFile(path) || client.removeDirectory(path))
				response = "\"" + path + "\" has been deleted.";
			else
				response = "\"" + path + "\" could not be deleted or does not exist.";
		}
		
		catch(IOException e)
		{
			response = "An IO error occured while sending the command or receiving its result.";
		}
		
		return response;
	}
	
	
	
	/**
	 * Download a file.
	 * 
	 * @param client The FTP client.
	 * @param path The path of the file to download
	 * 
	 * @return How the command went.
	 */
	public static String download(FTPClient client, String path)
	{
		String response;
		
		try
		{
			client.enterLocalPassiveMode();

			/* We take the filename and working directory from the full path */
			String workingDirectory = path.substring(0, path.lastIndexOf("/"));
			String filename         = path.substring(   path.lastIndexOf("/")+1);
			
			/* We create the file and its data */
			File file = new File("downloads/" + filename);
			while(file.exists())
			{
				String name      = filename.substring(0, filename.lastIndexOf("."));
				String extension = filename.substring(   filename.lastIndexOf("."));
				file = new File("downloads/" + name + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + extension);
			}
			OutputStream fileOutputStream = new FileOutputStream(file);
			
			/* We move to the working directory and retrieve the file */
			if(client.changeWorkingDirectory(workingDirectory) && client.retrieveFile(filename, fileOutputStream))
				response = "The download is a success.";
			else
			{
				file.delete();
				response = "The download failed.";
			}
			
			fileOutputStream.close();
		}
		
		catch(IOException e)
		{
			response = "An IO error occured while sending the command or receiving its result.";
		}
		
		return response;
	}
	
	
	
	/**
	 * List information about a file or files in a directory on the FTP server.
	 * 
	 * @param client The FTP client.
	 * @param path The path of the file or directory.
	 * 
	 * @return Information about the file or files in the directory.
	 */
	public static String list(FTPClient client, String path)
	{
		String response = "";
		
		try
		{
			client.enterLocalPassiveMode();
			
			FTPFile[] files = client.listFiles(path);
			
			for(FTPFile file : files)
			{
				response += file.getRawListing();
				response += "\n";
			}
			
			return response;
		}
		
		catch(IOException e)
		{
			response = "An IO error occured while sending the command or receiving its result.";
		}
		
		return response;
	}
	
	
	
	/**
	 * Rename a file or directory on the FTP server.
	 * 
	 * @param client The FTP client.
	 * @param from The path of the file or directory to rename.
	 * @param to The new name of the file or directory.
	 * 
	 * @return How the command went.
	 */
	public static String rename(FTPClient client, String from, String to)
	{
		String response;
		
		try
		{
			if(client.rename(from, to))
				response = "\"" + from + "\" has been renamed to \"" + to + "\".";
			else
				response = "\"" + from + "\" could not be renamed.";
		}
		
		catch(IOException e)
		{
			response = "An IO error occured while sending the command or receiving its result.";
		}
		
		return response;
	}
	
	
	
	/**
	 * Store a file or directory on the FTP server.
	 * 
	 * @param client The FTP client.
	 * @param from The input stream of the file or directory to upload.
	 * @param to The path where to store the input stream.
	 * 
	 * @return How the command went.
	 */
	public static String upload(FTPClient client, String from, String to)
	{
		String response;
		
		try
		{
			InputStream fileInputStream = new FileInputStream(new File(from));
			
			client.enterLocalPassiveMode();

			if(client.storeFile(to, fileInputStream))
				response = "The upload is a success.";
			else
				response = "The upload failed.";

			fileInputStream.close();
		}
		
		catch(IOException e)
		{
			response = "An IO error occured while sending the command or receiving its result.";
		}
		
		return response;
	}
}
