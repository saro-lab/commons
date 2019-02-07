package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

public class FilesTest {
    @Test
    public void test() throws Exception {
        
    }
    
    public void createTest() throws Exception {
        Files.createFile(new File("C:/abc/def/hi.txt"), true, "안녕", "UTF-8");
    }
    
    @Test
    public void toFileExt() {
        assertEquals(Files.toFileExt(""), "");
        assertEquals(Files.toFileExt("C:/aaa"), "");
        assertEquals(Files.toFileExt("C:\\aaa.bbb/abc.exe"), "exe");
        assertEquals(Files.toFileExt("/abc/def/ccc.gif"), "gif");
        assertEquals(Files.toFileExt("cccd.PNG"), "png");
        
        assertEquals(Files.toFileExt(new File("")), "");
        assertEquals(Files.toFileExt(new File("C:/aaa")), "");
        assertEquals(Files.toFileExt(new File("C:\\aaa.bbb/abc.exe")), "exe");
        assertEquals(Files.toFileExt(new File("/abc/def/ccc.gif")), "gif");
        assertEquals(Files.toFileExt(new File("cccd.PNG")), "png");
    }
    
    @Test
    public void validFileExt() {
        assertEquals(Files.validFileExt(new File("C:\\aaa.bbb/abc.exe"), "png", "gif", "exe"), true);
        assertEquals(Files.validFileExt(new File("C:\\aaa.bbb/abc.exe"), "png", "gif"), false);
        assertEquals(Files.validFileExt(new File("cccd.PNG"), "gif", "png"), true);
        assertEquals(Files.validFileExt(new File("cccd.PNG"), "gif", "PNG"), false);
    }
    
    public void remove() {
        
        // before 24 hour
        long before24hour = DateFormat.now().addHours(-24).getTimeInMillis();
        
        Files
            // get files in /testpath
            .listFilesStream("/testpath")
            // filter : older then 24 hours
            .filter(Files.attributesFilter(attr -> attr.creationTime().toMillis() < before24hour))
            // delete
            .forEach(File::delete);
    }
}
