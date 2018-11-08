package me.saro.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import me.saro.commons.ftp.FTP;

public class FtpTest {

    @Test
    public void test() throws Exception {
          example();
    }

    public void example() throws IOException {
        
        String host = "localhost";
        int port = 21; // FTP 21, FTPS 990, SFTP 22
        String user = "testuser";
        String pass = "test";
        
        String path1 = "C:/test/out";
        String path2 = "C:/test/in";
        
        new File(path1).mkdirs();
        new File(path2).mkdirs();
        
        try (FileOutputStream fos = new FileOutputStream(path1+"/test.dat")) {
            fos.write("the test file".getBytes());
        }
        
        try (FTP ftp = FTP.openFTP(host, port, user, pass)) {
            
            System.out.println("==================================");
            System.out.println("## now path");
            System.out.println(ftp.path());
            System.out.println("## listDirectories");
            ftp.listDirectories().forEach(e -> System.out.println(e));
            System.out.println("## listFiles");
            ftp.listFiles().forEach(e -> System.out.println(e));
            System.out.println("==================================");
            
            // send file
            ftp.send(new File(path1+"/test.dat"));
            ftp.send("test-new", new File(path1+"/test.dat"));
            
            // mkdir
            ftp.mkdir("tmp");
            
            // move
            String pwd = ftp.path();
            ftp.path(pwd+"/tmp");
            
            System.out.println("==================================");
            System.out.println("## now path");
            System.out.println(ftp.path());
            System.out.println("==================================");
            
            // move
            ftp.path(pwd);
            
            System.out.println("==================================");
            System.out.println("## now path");
            System.out.println(ftp.path());
            System.out.println("## listDirectories");
            ftp.listDirectories().forEach(e -> System.out.println(e));
            System.out.println("## listFiles");
            ftp.listFiles().forEach(e -> System.out.println(e));
            System.out.println("==================================");
            
            // recv file
            ftp.recv("test.dat", new File(path2+"/test.dat"));
            ftp.recv("tmp", new File(path2+"/tmp")); // is not file, return false; not recv
            
            // delete
            //ftp.delete("tmp");
            //ftp.delete("test-new");
            //ftp.delete("test.dat");
            
            System.out.println("==================================");            
            System.out.println("## listDirectories");
            ftp.listDirectories().forEach(e -> System.out.println(e));
            System.out.println("## listFiles");
            ftp.listFiles().forEach(e -> System.out.println(e));
            System.out.println("==================================");
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

