package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import me.saro.commons.__old.bytes.Naming;
import org.junit.jupiter.api.Test;

public class NamingTest {

    @Test
    public void toWords() throws Exception {
        assertEquals(Naming.toWordsByPascalCase("AbcDef123"), Arrays.asList("Abc", "Def123"));
        assertEquals(Naming.toWordsByCamelCase("abcDEF123Def"), Arrays.asList("abc", "DEF123", "Def"));
        assertEquals(Naming.toWordsByUnderscores("ABC_def_123"), Arrays.asList("ABC", "def", "123"));
        assertEquals(Naming.toWordsByDashes("abc-def-123"), Arrays.asList("abc", "def", "123"));
    }
    
    @Test
    public void toNames() throws Exception {
        List<String> words = Arrays.asList("first32", "abc", "WORD");
        
        assertEquals(Naming.toPascalCase(words), "First32AbcWORD");
        assertEquals(Naming.toCamelCase(words), "first32AbcWORD");
        assertEquals(Naming.toUnderscores(words), "first32_abc_WORD");
        assertEquals(Naming.toDashes(words), "first32-abc-WORD");
        
        assertEquals(Naming.toCamelCase(Arrays.asList("abc", "DEF123", "Def")), "abcDEF123Def");
    }
}
