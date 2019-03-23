package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import me.saro.commons.json.JsonReader;

public class JsonReaderTest {

    @Test
    public void test() throws Exception {

        JsonReader json;

        json = new JsonReader("[]");
        assertTrue(json.isArray());

        json = new JsonReader("{}");
        assertTrue(json.isObject());

        json = new JsonReader("{\"a\":\"1\"}");
        assertEquals("1", json.getString("a"));
        assertEquals(1, json.getInt("a", -1));

        json = new JsonReader("[{},{},{},{}]");
        assertEquals(4, json.length());

        json = new JsonReader("[{\"a\":\"1\"}]").get(0);
        assertEquals("1", json.getString("a"));
        assertEquals(1, json.getInt("a", -1));

        json = new JsonReader("[{\"a\":\"1\"}]").toList().get(0);
        assertEquals("1", json.getString("a"));
        assertEquals(1, json.getInt("a", -1));

        json = new JsonReader("{\"x\":{\"a\":\"1\"}}").into("x");
        assertEquals("1", json.getString("a"));
        assertEquals(1, json.getInt("a", -1));

        json = new JsonReader("[{\"a\":\"1\"}]").toList().get(0);
        assertEquals("{\"a\":\"1\"}", json.toString());

        assertEquals( new JsonReader("[{\"a\":\"1\"}]"),  new JsonReader("[{\"a\":\"1\"}]"));

    }
}
