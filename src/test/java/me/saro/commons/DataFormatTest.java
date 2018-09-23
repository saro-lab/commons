package me.saro.commons;

import static org.junit.Assert.*;

import org.junit.Test;

public class DataFormatTest {
	
	@Test
    public void test() throws Exception {
		
		assertEquals(DateFormat.parse("2018-09-23 21:53:00", "yyyy-MM-dd HH:mm:ss"), DateFormat.parse("2018-09-23 21:53:00", "yyyy-MM-dd HH:mm:ss"));
		
		assertNotEquals(DateFormat.parse("2018-09-23 21:53:00", "yyyy-MM-dd HH:mm:ss"), DateFormat.parse("2018-09-24 21:53:00", "yyyy-MM-dd HH:mm:ss"));
    }
	
}
