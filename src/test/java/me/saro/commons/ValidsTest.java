package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
