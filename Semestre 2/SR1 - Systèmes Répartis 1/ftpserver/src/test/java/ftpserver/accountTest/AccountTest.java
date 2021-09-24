package ftpserver.accountTest;



import static org.junit.Assert.assertEquals;

import org.junit.Before;

import ftpserver.account.Account;



public class AccountTest
{
	
	private Account account;
	
	
	
	@Before
	public void init()
	{
		account = new Account("rayane", "hamani");
	}
	
	
	
	public void getUsernameTest()
	{
		assertEquals(account.getUsername(), "rayane");
	}
	
	public void getPasswordTest()
	{
		assertEquals(account.getPassword(), "hamani");
	}
	
}
