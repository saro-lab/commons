package me.saro.commons;

import org.junit.Test;
import static org.junit.Assert.*;

public class ValidsTest {
	
	@Test
    public void test() throws Exception {
		
		assertTrue(Valids.isMail("abc@saro.me", 64));
		assertTrue(Valids.isMail("abc@localhost.com", 64));
		assertTrue(Valids.isMail("a_-b@abc.com", 64));
		assertTrue(Valids.isMail("1a_-b@abc.com", 64));
		assertFalse(Valids.isMail("name@localhost", 64));
		assertFalse(Valids.isMail("name@localhost.m", 64));
		assertFalse(Valids.isMail("@localhost.com", 64));
    }
	
}
