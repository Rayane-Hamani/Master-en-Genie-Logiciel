package ftpserver.command;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class Stor
{
	
	/**
	 * Store a file
	 * 
	 * @param reader the reader of the passive connection
	 * @param path the path of the working directory
	 * @param filename the path of the file to store, from the working directory
	 * 
	 * @return the status of how the command went
	 */
	public static String execute(BufferedReader reader, String path, String filename)
	{
		File file = new File(path, filename);
		
		if(file.exists())
			return execute(reader, path, filename + "-2");
		
		try {
			if(file.createNewFile())
			{
				FileWriter fileWriter = new FileWriter(file);
				
				String data = "";
				String dataTmp;
				while((dataTmp = reader.readLine()) != null)
					data += dataTmp;
				
				fileWriter.write(data);
				fileWriter.close();
				
				return "250 File transfered.";
			}
			else
				return "530 The file couldn't be stored.";
		}
		catch (IOException e)
		{
			return "530 The file couldn't be stored.";
		}
	}
	
	/**
	 * Return if the status of the command tells the command ran successfully
	 * 
	 * @param status the status of the command
	 * 
	 * @return if the command ran successfully
	 */
	public static boolean isSuccessful(String status)
	{
		return status.startsWith("250");
	}
	
}
