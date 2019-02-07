package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void executeAllThreads() throws Exception {
        
        List<String> seqs = IntStream.range(1, 11).mapToObj(e -> Integer.toString(e)).collect(Collectors.toList());
        
        List<String> rv = Utils.executeAllThreads(10, seqs, e -> e + "P");
        
        assertEquals("10P1P2P3P4P5P6P7P8P9P", rv.stream().sorted().collect(Collectors.joining("")));
    }

    @Test
    public void zerofill() {
        assertEquals(Utils.zerofill(31, 4), "0031");
        assertEquals(Utils.zerofill(313241982L, 20), "00000000000313241982");
        assertEquals(Utils.zerofill("38", 2), "38");
        assertEquals(Utils.zerofill("318", 4), "0318");
        assertThrows(IllegalArgumentException.class, () -> Utils.zerofill(1, 0));
        assertThrows(IllegalArgumentException.class, () -> Utils.zerofill(-1, 10));
        assertThrows(IllegalArgumentException.class, () -> Utils.zerofill("asdf", 10));
        assertThrows(IllegalArgumentException.class, () -> Utils.zerofill(3124, 2));
    }
}
