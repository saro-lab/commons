package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.LinkedHashMap;
import java.util.Map;

import me.saro.commons.__old.bytes.Converter;
import me.saro.commons.__old.bytes.Maps;
import org.junit.jupiter.api.Test;

public class MapsTest {
    
    @Test
    public void pick() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        
        Map<String, Integer> pick = Maps.pick(map, "A", "C");
        
        assertEquals(Converter.toJson(pick), "{\"A\":1,\"C\":3}");
    }
    
    @Test
    public void cloneTest() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        
        Map<String, Integer> clone = Maps.clone(map);
        
        map.put("E", 5);
        
        assertNull(clone.get("E"));
        
        assertEquals((int)clone.get("A"), 1);
        assertEquals((int)clone.get("B"), 2);
        assertEquals((int)clone.get("C"), 3);
        assertEquals((int)clone.get("D"), 4);
    }
    
    @Test
    public void filter() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        
        Map<String, Integer> filter = Maps.filter(map, e -> e.getKey().equals("A"));
        
        assertEquals(Converter.toJson(filter), "{\"A\":1}");
    }
    
}
