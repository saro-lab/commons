package me.saro.commons;

import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import me.saro.commons.__old.bytes.ssh.SSHExecutor;
import me.saro.commons.__old.bytes.ssh.SSHShell;

public class SSHTest {
    
    String host = "192.168.0.13";
    int port = 22;
    String user = "saro";
    String pass = "testtest";
    String charset = "utf-8";
    
    @Test
    public void test() throws Exception {
        // executor1();
        // executor2();
        // shell1();
        // shell2();
    }

    public void executor1() throws IOException {
        try (SSHExecutor ssh = SSHExecutor.open(host, port, user, pass, charset) ; Scanner sc = new Scanner(System.in)) {

            System.out.println("connected :");

            input : while ( true ) {
                String line = sc.nextLine();
                
                if (line.equals("exit")) {
                    break input;
                }

                System.out.println(ssh.cmd(line));
            }

        }
    }
    
    public void executor2() throws IOException {
        String result = SSHExecutor.just(host, port, user, pass, charset, "ls -al");
        System.out.println(result);
    }

    public void shell1() throws IOException {
        try (SSHShell ssh = SSHShell.open(host, port, user, pass, charset, System.out::println) ; Scanner sc = new Scanner(System.in)) {

            input : while ( true ) {
                String line = sc.nextLine();

                ssh.cmd(line);

                if ("exit".equals(line.trim())) {
                    
                    break input;
                }
            }
        
        }
    }
    
    public void shell2() throws IOException {
        try (SSHShell ssh = SSHShell.open(host, port, user, pass, charset, System.out::println)) {
            ssh.cmd("ls", "ls -al", "ps -ef", "ll");
        }
    }
}
