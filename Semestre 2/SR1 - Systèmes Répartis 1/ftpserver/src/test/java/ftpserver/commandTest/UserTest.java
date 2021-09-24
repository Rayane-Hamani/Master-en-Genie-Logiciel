package ftpserver.commandTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ftpserver.account.Account;
import ftpserver.account.AccountUtil;
import ftpserver.command.User;

public class UserTest
{
	
	@Before
	public void init()
	{
		AccountUtil.setAccounts("rayane", new Account("rayane", "hamani"));
		AccountUtil.setAccounts("florian", new Account("florian", "dendoncker"));
		AccountUtil.setAccounts("anonymous", new Account("anonymous", ""));
	}
	
	@Test
	public void executeWhenRegisteredUsername()
	{
		String response = User.execute("rayane");
		assertEquals(response, "331 Please specify the password.");
	}
	
	@Test
	public void executeWhenNotRegisteredUsername()
	{
		String response = User.execute("coucou");
		assertEquals(response, "530 User not registered. This server can be accessed by anonymous");
	}
	
	@Test
	public void isSuccessfulWhenTrue()
	{
		String response = User.execute("rayane");
		assertTrue(response.startsWith("331"));
	}
	
	@Test
	public void isSuccessfulWhenFalse()
	{
		String response = User.execute("coucou");
		assertFalse(response.startsWith("331"));
	}
}
