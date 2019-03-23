package me.saro.commons;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * naming class
 * @author      PARK Yong Seo
 * @since       3.0
 */
public class Naming {
    
    private static Stream<String> normalize(List<String> word) {
        if (word == null) {
            return Stream.empty();
        }
        return word.stream().filter(e -> e != null).map(e -> e.trim()).filter(e -> !e.isEmpty());
    }
    
    private static String normalize(String name) {
        if (name == null) {
            return "";
        }
        return name.trim();
    }
    
    /**
     * toPascalCase
     * @param words
     * @return
     */
    public static String toPascalCase(List<String> words) {
        return normalize(words).map(e -> Character.toUpperCase(e.charAt(0)) + e.substring(1)).collect(Collectors.joining());
    }
    
    /**
     * toWordsByPascalCase
     * @param name
     * @return
     */
    public static List<String> toWordsByPascalCase(String name) {
        if ((name = normalize(name)).isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(name.replaceAll("([^A-Z])([A-Z])", "$1 $2").split("\\s+"));
    }
    
    /**
     * toCamelCase
     * @param words
     * @return
     */
    public static String toCamelCase(List<String> words) {
        List<String> tmp = normalize(words).map(e -> Character.toUpperCase(e.charAt(0)) + e.substring(1)).collect(Collectors.toList());
        if (tmp.isEmpty()) {
            return "";
        }
        tmp.set(0, tmp.get(0).toLowerCase());
        return tmp.stream().collect(Collectors.joining());
    }
    
    /**
     * toWordsByCamelCase
     * @param name
     * @return
     */
    public static List<String> toWordsByCamelCase(String name) {
        if ((name = normalize(name)).isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(name.replaceAll("([^A-Z])([A-Z])", "$1 $2").split("\\s+"));
    }
    
    /**
     * toUnderscores
     * @param words
     * @return
     */
    public static String toUnderscores(List<String> words) {
        return normalize(words).collect(Collectors.joining("_"));
    }
    
    /**
     * toWordsByUnderscores
     * @param name
     * @return
     */
    public static List<String> toWordsByUnderscores(String name) {
        return Arrays.asList(normalize(name).split("\\_"));
    }
    
    /**
     * toDashes
     * @param words
     * @return
     */
    public static String toDashes(List<String> words) {
        return normalize(words).collect(Collectors.joining("-"));
    }
    
    /**
     * toWordsByDashes
     * @param name
     * @return
     */
    public static List<String> toWordsByDashes(String name) {
        return Arrays.asList(normalize(name).split("\\-"));
    }
}
