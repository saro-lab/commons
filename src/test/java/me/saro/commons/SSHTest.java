package me.saro.commons;

import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.jcraft.jsch.JSchException;

public class SSHTest {
    @Test
    public void test() throws Exception {
          // example();
    }

    public void example() throws IOException, InterruptedException, JSchException {
        try (SSH ssh = SSH.open("localhost", 22, "tempuser", "password", "utf-8")) {
            
            Scanner sc = new Scanner(System.in);
            String line;
            
            System.out.println("connected :");
            
            input : while ( (line = sc.nextLine()) != null ) {
                if (line.equals("exit")) {
                    sc.close();
                    break input;
                }
                
                System.out.println(ssh.cmd(line));
            }
            
        }
    }
}
