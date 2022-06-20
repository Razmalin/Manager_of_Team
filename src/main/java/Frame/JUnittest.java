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
		Assert.assertTrue(Util.isEmpty(null)); // Проверяем на null
		Assert.assertTrue(Util.isEmpty("")); // Проверяем на пусто
	}
	@Test
	public void testNonisEmpty() {
		Assert.assertFalse(Util.isEmpty(" ")); // Проверяем на пробел
		Assert.assertFalse(Util.isEmpty("some string")); // Проверяем на непустую строку
	}
	@Test
	public void testSum() {
	Assert.assertEquals(4, Util.sum(2, 2)); 
	Assert.assertNotEquals(5, Util.sum(2, 2)); 
	}
	@Test(expected = RuntimeException.class) // Проверяем на появление исключения
	public void testException() {
		throw new RuntimeException("Error");
	}
	@BeforeClass // Фиксируем начало тестирования
	 public static void allTestsStarted() {
		System.out.println("Start testing");
	 }
	 @AfterClass // Фиксируем конец тестирования
	 public static void allTestsFinished() {
		 System.out.println("End of testing");
	 }
	 @Before // Фиксируем запуск теста
	 public void testStarted() {
		 System.out.println("Test run");
	 }
	 @After // Фиксируем завершение теста
	 public void testFinished() {
		 System.out.println("Completion of the test");
	 }
}
