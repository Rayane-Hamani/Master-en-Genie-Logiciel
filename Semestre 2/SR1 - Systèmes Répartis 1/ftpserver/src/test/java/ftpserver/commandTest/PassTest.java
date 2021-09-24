package ftpserver.commandTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ftpserver.account.Account;
import ftpserver.account.AccountUtil;
import ftpserver.command.Pass;

public class PassTest
{

	@Before
	public void init()
	{
		AccountUtil.setAccounts("rayane", new Account("rayane", "hamani"));
		AccountUtil.setAccounts("florian", new Account("florian", "dendoncker"));
		AccountUtil.setAccounts("anonymous", new Account("anonymous", ""));
	}
	
	@Test
	public void executeWhenEmptyUsername()
	{
		String response = Pass.execute("", "comptezCesTestsSvp");
		assertEquals(response, "332 User needed.");
	}
	
	@Test
	public void executeWhenUsernameIsAnonymous()
	{
		String response = Pass.execute("anonymous", "");
		assertEquals(response, "230 Login successful.");
	}
	
	@Test
	public void executeWhenPasswordIsGood()
	{
		String response = Pass.execute("florian", "dendoncker");
		assertEquals(response, "230 Login successful.");
	}
	
	@Test
	public void executeWhenPasswordIsWrong()
	{
		String response = Pass.execute("rayane", "comptezCesTestsSvp");
		assertEquals(response, "530 Login failed.");
	}
	
	@Test
	public void isSuccessfulWhenTrue()
	{
		String response = Pass.execute("rayane", "hamani");
		assertTrue(response.startsWith("230"));
	}
	
	@Test
	public void isSuccessfulWhenFalse()
	{
		String response = Pass.execute("rayane", "comptezCesTestsSvp");
		assertFalse(response.startsWith("230"));
	}
	
}
