package me.saro.commons;

import static org.junit.Assert.*;

import org.junit.Test;

public class JsonReaderTest {
	
	@Test
    public void test() throws Exception {
		
		JsonReader json;
		
		json = JsonReader.create("[]");
		assertTrue(json.isArray());
		
		json = JsonReader.create("{}");
		assertTrue(json.isObject());
		
		json = JsonReader.create("{\"a\":\"1\"}");
		assertEquals("1", json.getString("a"));
		assertEquals(1, json.getInt("a", -1));
		
		json = JsonReader.create("[{},{},{},{}]");
		assertEquals(4, json.length());
		
		json = JsonReader.create("[{\"a\":\"1\"}]").get(0);
		assertEquals("1", json.getString("a"));
		assertEquals(1, json.getInt("a", -1));
		
		json = JsonReader.create("[{\"a\":\"1\"}]").toList().get(0);
		assertEquals("1", json.getString("a"));
		assertEquals(1, json.getInt("a", -1));
		
		json = JsonReader.create("{\"x\":{\"a\":\"1\"}}").into("x");
		assertEquals("1", json.getString("a"));
		assertEquals(1, json.getInt("a", -1));
		
		json = JsonReader.create("[{\"a\":\"1\"}]").toList().get(0);
		assertEquals("{\"a\":\"1\"}", json.toString());
		
		assertEquals( JsonReader.create("[{\"a\":\"1\"}]"),  JsonReader.create("[{\"a\":\"1\"}]"));
    }
	
}
