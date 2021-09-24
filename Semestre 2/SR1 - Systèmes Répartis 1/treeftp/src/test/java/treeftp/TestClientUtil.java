package treeftp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class TestClientUtil
{
	@Test(expected = NoArgsException.class)
	public void testCreateClientWith0Args() throws NoArgsException
	{
		String[] args = { };

		ClientUtil.createClient(args);
		fail("Should not reach this code.");
	}
	
	public void testCreateClientWith1Args()
	{
		String[] args = { "ftp.ubuntu.com" };
		
		Client client1 = new Client("ftp.ubuntu.com", "anonymous", "");
		Client client2 = null;
		try
		{
			client2 = ClientUtil.createClient(args);
		}
		catch(NoArgsException e)
		{
			/* we don't need to do anything here because
			 * it means the assert will fail */
		}
		
		assertEquals(client1, client2);
	}
	
	public void testCreateClientWith4Args()
	{
		String[] args = { "ftp.ubuntu.com", "/", "1", "username" };
		
		Client client1 = new Client("ftp.ubuntu.com", "username", "");
		Client client2 = null;
		try
		{
			client2 = ClientUtil.createClient(args);
		}
		catch(NoArgsException e)
		{
			/* we don't need to do anything here because
			 * it means the assert will fail */
		}
		
		assertEquals(client1, client2);
	}
	
	public void testCreateClientWithMoreOf4Args()
	{
		String[] args = { "ftp.ubuntu.com", "/", "1", "username", "password" };
		
		Client client1 = new Client("ftp.ubuntu.com", "username", "password");
		Client client2 = null;
		try
		{
			client2 = ClientUtil.createClient(args);
		}
		catch(NoArgsException e)
		{
			/* we don't need to do anything here because
			 * it means the assert will fail */
		}
		
		assertEquals(client1, client2);
	}
	
	public void testGetAddressAndPort()
	{
		String response = "227 Entering Passive Mode (0,1,2,3,4,5)";
		
		String[] responseSplit = ClientUtil.getAddressAndPort(response);
		String[] expected = { "0","1","2","3","4","5" };
		
		for(int i=0; i<expected.length; i++)
		{
			assertEquals(responseSplit[i], expected[i]);
		}
	}
	
	public void testGetAddress()
	{
		String response = "227 Entering Passive Mode (0,1,2,3,4,5)";
		String[] responseSplit = ClientUtil.getAddressAndPort(response);
		
		String address = ClientUtil.getAddress(responseSplit);
		String expected = "0.1.2.3";
		
		assertEquals(address, expected);
	}
	
	public void testGetPort()
	{
		String response = "227 Entering Passive Mode (0,1,2,3,4,5)";
		String[] responseSplit = ClientUtil.getAddressAndPort(response);
		
		int port = ClientUtil.getPort(responseSplit);
		int expected = (4 * 256) + 5;
		
		assertEquals(port, expected);
	}
	
	public void testIfNotASuccessfulResponseWhenResponseIsASuccess()
	{
		int actual = 0;
		int expected = 1;
		try
		{
			ClientUtil.ifNotASuccessfulResponse("999", "999", "");
			actual++;
		}
		catch(IOException e)
		{
		}
		
		/* If the response is really a success
		 * then actual should be incremented by 1
		 * and match expected */
		assertEquals(actual, expected);
	}
	
	@Test (expected = IOException.class)
	public void testIfNotASuccessfulResponseWhenResponseIsAFail() throws IOException
	{
		ClientUtil.ifNotASuccessfulResponse("111", "999", "coucou ");
		fail("Should not reach this code.");
	}
}
