package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void executeAllThreads() throws Exception {
        
        List<String> seqs = IntStream.range(1, 11).mapToObj(e -> Integer.toString(e)).collect(Collectors.toList());
        
        List<String> rv = Utils.executeAllThreads(10, seqs, e -> {
            System.out.println(e + " START");
            Thread.sleep(5000);
            System.out.println(e + " END");
            return e + "P";
        });
        
        assertEquals("10P1P2P3P4P5P6P7P8P9P", rv.stream().sorted().collect(Collectors.joining("")));
    }

}
