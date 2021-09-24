package treeftp;

public class Color
{
	public static final String ANSI_RESET      = "\u001B[0m"  ;
	public static final String ANSI_BOLD       = "\u001B[1m"  ;
	
	public static final String ANSI_RED_TEXT   = "\u001B[31m" ;
	public static final String ANSI_GREEN_TEXT = "\u001B[32m" ;
	public static final String ANSI_BLUE_TEXT  = "\u001B[34m" ;
	public static final String ANSI_WHITE_TEXT = "\u001B[37m" ;

	
	
	/**
	 * Return the name of the file and its link if it has one, colored according to their type
	 * 
	 * @param filetype the type of the file
	 * @param filename the name of the file and its link if it has one
	 * 
	 * @return the name of the file and its link if it has one, colored
	 */
	public static String colorText(String filetype, String filename)
	{
		String filenameColored;
		
		switch(filetype)
		{
			case "d" :
				filenameColored  = ANSI_BOLD  + ANSI_BLUE_TEXT  + filename + ANSI_RESET;
				break;
			case "l" :
				String[] filenameSplit = filename.split(" -> ");
				filenameColored  = ANSI_BOLD  + ANSI_GREEN_TEXT + filenameSplit[0] + ANSI_RESET;
				filenameColored += ANSI_WHITE_TEXT + " -> ";
				// le lien est absolu donc il est possible d'atteindre directement le fichier pointé et vérifier son type avant de remonter dans le dossier où on était
				filenameColored += colorText("-", filenameSplit[1]);
				break;
			default :
				filenameColored  = ANSI_WHITE_TEXT + filename + ANSI_RESET;
				break;
		}
		
		return filenameColored;
	}
}
