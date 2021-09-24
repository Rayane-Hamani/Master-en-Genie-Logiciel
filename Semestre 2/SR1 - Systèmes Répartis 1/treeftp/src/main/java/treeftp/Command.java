package treeftp;

public class Command
{
	public static String user(String username) { return "USER " + username + "\r\n" ;}
	public static String pass(String password) { return "PASS " + password + "\r\n" ;}
	public static String pasv()                { return "PASV"             + "\r\n" ;}
	public static String list()                { return "LIST"             + "\r\n" ;}
	public static String cwd(String directory) { return "CWD " + directory + "\r\n" ;}
	public static String quit()                { return "QUIT"             + "\r\n" ;}
}