package Frame;

import org.junit.Test;
import Frame.App.Util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

public class JUnittest {
	@Test
	public void testisEmpty() {
		Assert.assertTrue(Util.isEmpty(null)); // ��������� �� null
		Assert.assertTrue(Util.isEmpty("")); // ��������� �� �����
	}
	@Test
	public void testNonisEmpty() {
		Assert.assertFalse(Util.isEmpty(" ")); // ��������� �� ������
		Assert.assertFalse(Util.isEmpty("some string")); // ��������� �� �������� ������
	}
	@Test
	public void testSum() {
	Assert.assertEquals(4, Util.sum(2, 2)); 
	Assert.assertNotEquals(5, Util.sum(2, 2)); 
	}
	@Test(expected = RuntimeException.class) // ��������� �� ��������� ����������
	public void testException() {
		throw new RuntimeException("Error");
	}
	@BeforeClass // ��������� ������ ������������
	 public static void allTestsStarted() {
		System.out.println("Start testing");
	 }
	 @AfterClass // ��������� ����� ������������
	 public static void allTestsFinished() {
		 System.out.println("End of testing");
	 }
	 @Before // ��������� ������ �����
	 public void testStarted() {
		 System.out.println("Test run");
	 }
	 @After // ��������� ���������� �����
	 public void testFinished() {
		 System.out.println("Completion of the test");
	 }
}
