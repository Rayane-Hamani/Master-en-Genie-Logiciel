package treeftp;

import static org.junit.Assert.assertEquals;

public class TestTreeUtil
{
	public void testGetFiletype()
	{
		String fileStatus = "drwxrwxr-x 4 rayane rayane 4096 janv. 19 22:51 file";
		
		String filetype = TreeUtil.getFiletype(fileStatus);
		String expected = "d";
		
		assertEquals(filetype, expected);
	}
	
	public void testGetFilenameWhenASymlink()
	{
		String fileStatus = "lrwxrwxr-x 4 rayane rayane 4096 janv. 19 22:51 file -> coucou";
		
		String filetype = TreeUtil.getFiletype(fileStatus);
		String expected = "file";
		
		assertEquals(filetype, expected);
	}
	
	public void testGetFilenameWhenNotASymlink()
	{
		String fileStatus = "drwxrwxr-x 4 rayane rayane 4096 janv. 19 22:51 file";
		
		String filetype = TreeUtil.getFiletype(fileStatus);
		String expected = "file";
		
		assertEquals(filetype, expected);
	}
	
	public void testIndentation()
	{
		String actual = TreeUtil.indentation(2);
		String expected = "│    │    ";
		
		assertEquals(actual, expected);
	}
}
