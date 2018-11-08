package me.saro.commons;

import java.io.File;

import org.junit.jupiter.api.Test;

import me.saro.commons.ftp.FTP;

public class FtpTest {

    @Test
    public void test() throws Exception {
          //example();
    }

    public void example() {
        // 
        String host = "host";
        int port = 0;
        String user = "user";
        String pass = "pass";
        
        String path1 = "";
        String path2 = "";
        
        try (FTP ftp = FTP.openSFTP(host, port, user, pass)) {
            
            System.out.println("==================================");
            System.out.println("now path");
            System.out.println(ftp.path());
            
            System.out.println("listDirectories");
            ftp.listDirectories().forEach(e -> System.out.println(e));
            
            System.out.println("listFiles");
            ftp.listFiles().forEach(e -> System.out.println(e));
            System.out.println("==================================");
            
            // send file
            ftp.send(new File(path1+"test.dat"));
            ftp.send("test-new", new File(path1+"test.dat"));
            
            // mkdir
            ftp.mkdir("tmp");
            
            // move
            String pwd = ftp.path();
            ftp.path(pwd+"/tmp");
            
            System.out.println("==================================");
            System.out.println("now path");
            System.out.println(ftp.path());
            System.out.println("==================================");
            
            // move
            ftp.path(pwd);
            
            System.out.println("==================================");
            System.out.println("now path");
            System.out.println(ftp.path());
            
            System.out.println("listDirectories");
            ftp.listDirectories().forEach(e -> System.out.println(e));
            
            System.out.println("listFiles");
            ftp.listFiles().forEach(e -> System.out.println(e));
            System.out.println("==================================");
            
            // recv file
            ftp.recv("test.dat", new File(path2+"test.dat"));
            ftp.recv("tmp", new File(path2+"tmp")); // is not file, return false; not recv
            
            // delete
            ftp.delete("tmp");
            ftp.delete("test-new");
            ftp.delete("test.dat");
            
            System.out.println("==================================");            
            System.out.println("listDirectories");
            ftp.listDirectories().forEach(e -> System.out.println(e));
            
            System.out.println("listFiles");
            ftp.listFiles().forEach(e -> System.out.println(e));
            System.out.println("==================================");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

