package treeftp;

import static org.junit.Assert.assertEquals;

public class testCommand
{
	public void testUser()
	{
		String username = "coucou";
		
		String actual = Command.user(username);
		String expected = "USER coucou\r\n";
		
		assertEquals(actual, expected);
	}
	
	public void testPass()
	{
		String password = "coucou";
		
		String actual = Command.user(password);
		String expected = "PASS coucou\r\n";
		
		assertEquals(actual, expected);
	}
	
	public void testPasv()
	{
		String actual = Command.pasv();
		String expected = "PASV\r\n";
		
		assertEquals(actual, expected);
	}
	
	public void testList()
	{
		String actual = Command.pasv();
		String expected = "LIST\r\n";
		
		assertEquals(actual, expected);
	}
	
	public void testCwd()
	{
		String directory = "coucou";
		
		String actual = Command.cwd(directory);
		String expected = "CWD coucou\r\n";
		
		assertEquals(actual, expected);
	}
	
	public void testQuit()
	{
		String actual = Command.quit();
		String expected = "QUIT\r\n";
		
		assertEquals(actual, expected);
	}
}
