package me.saro.commons;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

/**
 * map class
 * @author      PARK Yong Seo
 * @since       2.2
 */
public class Maps {
    
    /**
     * pick the keys and make sub map
     * @param map
     * @param keys
     * @return
     */
    @SafeVarargs
    public static <K, V> Map<K, V> pick(Map<K, V> map, K... keys) {
        Map<K, V> rv = new LinkedHashMap<>();
        if (keys != null) {
            for (K key : keys) {
                rv.put(key, map.get(key));
            }
        }
        return rv;
    }
    
    /**
     * clone map<br>
     * it is not deep copy
     * @param map
     * @return
     */
    public static <K, V> Map<K, V> clone(Map<K, V> map) {
        Map<K, V> rv = new LinkedHashMap<>();
        for (Entry<K, V> entry : map.entrySet()) {
            rv.put(entry.getKey(), entry.getValue());
        }
        return rv;
    }
    
    /**
     * filter map
     * @param map
     * @param filter
     * @return
     */
    public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<Entry<K, V>> filter) {
        Map<K, V> rv = new LinkedHashMap<>();
        for (Entry<K, V> entry : map.entrySet()) {
            if (filter.test(entry)) {
                rv.put(entry.getKey(), entry.getValue());
            }
        }
        return rv;
    }
    
}
