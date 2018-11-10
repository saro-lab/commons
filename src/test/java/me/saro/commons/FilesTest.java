package me.saro.commons;

import java.io.File;

import org.junit.jupiter.api.Test;

public class FilesTest {
    @Test
    public void test() {
        
    }
    
    
    public void remove() {
        
        // before 24 hour
        long before24hour = DateFormat.now().addHours(-24).getTimeInMillis();
        
        Files
            // get files in /testpath
            .streamFiles("/testpath")
            // filter : older then 24 hours
            .filter(Files.attributesFilter(attr -> attr.creationTime().toMillis() < before24hour))
            // delete
            .forEach(File::delete);
    }
}
