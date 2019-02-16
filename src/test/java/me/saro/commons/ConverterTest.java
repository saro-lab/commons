package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import me.saro.commons.bytes.Bytes;
import me.saro.commons.converter.HashAlgorithm;
import me.saro.commons.converter.NamingConvention;

public class ConverterTest {

    @Test
    public void toMap() throws Exception {
        assertEquals(
            "a1b2c3",
            Converter.<String, Integer>toMap("a", 1, "b", 2, "c", 3).entrySet().stream()
            .map(e -> e.getKey() + e.getValue()).collect(Collectors.joining())
        );
    }
    
    @Test
    public void asList() {
        assertEquals("123", Converter.asList("1", "2", "3").stream().collect(Collectors.joining()));
    }
    
    @Test
    public void splitCsvLine() {
        assertEquals(5, Converter.splitCsvLine("aaa,bbb,ccc,\"ddd,eee\",fff").length);
    }
    
    @Test
    public void splitByToken() {
        assertEquals(Converter.splitByToken(null, ",").size(), 0);
        assertEquals(Converter.splitByToken("", "|").stream().collect(Collectors.joining("|")), "");
        assertEquals(Converter.splitByToken("hello", "|").stream().collect(Collectors.joining("|")), "hello");
        assertEquals(Converter.splitByToken("the|saro|||commons||working||", "|").stream().collect(Collectors.joining("|")), "the|saro|||commons||working||");
    }

    @Test
    public void toHash() {
        // Converter.HASH_ALGORITHM_SHA3* minimum java version is 10 
        assertEquals("2ce5bebfa51bf5b222a5c8977d3c1d37875703d3", Bytes.toHex(Converter.toHash(HashAlgorithm.SHA1, "SARO")));
    }

    @Test
    public void namingConvention() {
        assertEquals("fooBar", Converter.namingConvention(NamingConvention.camelCase, Arrays.asList("foo", "bar")));
        assertEquals("foo-bar", Converter.namingConvention(NamingConvention.dashes, Arrays.asList("foo", "bar")));
        assertEquals("FooBar", Converter.namingConvention(NamingConvention.pascalCase, Arrays.asList("foo", "bar")));
        assertEquals("foo_bar", Converter.namingConvention(NamingConvention.underscores, Arrays.asList("foo", "bar")));
    }
}
