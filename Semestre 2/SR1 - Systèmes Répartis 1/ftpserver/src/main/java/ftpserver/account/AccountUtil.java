package ftpserver.account;

import java.util.HashMap;
import java.util.Map;

public class AccountUtil
{
	private static Map<String, Account> accounts = new HashMap<String, Account>();
	
	/**
	 * Let the other classes to access in read the registered accounts
	 * 
	 * @return the pair username and account of the accounts
	 */
	public static Map<String, Account> getAccounts()
	{
		return accounts;
	}
	
	/**
	 * Let the other classes to access in write the registered accounts
	 * 
	 * @param username the username of the account
	 * @param account the account
	 */
	public static void setAccounts(String username, Account account)
	{
		accounts.put(username, account);
	}
	
	/**
	 * Return if a username is registered in the accounts
	 * 
	 * @param username the username
	 * @return if a username is registered in the accounts
	 */
	public static boolean isRegistered(String username)
	{
		return accounts.containsKey(username);
	}
	
}
