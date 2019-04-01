package me.saro.commons;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import me.saro.commons.shell.Shell;

public class ShellTest {

    @Test
    public void test() throws Exception {
          //example();
    }

    public void example() throws IOException {
        // windows cmd dir
        System.out.println(Shell.execute("cmd", "/c", "dir"));
    }
}

