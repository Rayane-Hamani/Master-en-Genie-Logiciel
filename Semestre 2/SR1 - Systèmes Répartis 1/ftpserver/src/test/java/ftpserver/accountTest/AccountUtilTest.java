package ftpserver.accountTest;



import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import ftpserver.account.Account;
import ftpserver.account.AccountUtil;



public class AccountUtilTest 
{
	
	@Test
	public void isRegisteredTestWhenTrue()
	{
		AccountUtil.setAccounts("rayane", new Account("rayane", "hamani"));
		assertTrue(AccountUtil.isRegistered("rayane"));
	}
	
	@Test
	public void isRegisteredTestWhenFalse()
	{
		assertFalse(AccountUtil.isRegistered("Je n'aime pas les tests mais je les fais quand même parce-que good practice oblige"));
	}

}
