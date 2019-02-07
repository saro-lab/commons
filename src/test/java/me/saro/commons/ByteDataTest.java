package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class ByteDataTest {
    
    final static byte SPACE = ' ';
    
    @Test
    public void create() throws IOException {
        assertEquals(ByteData.create().write("hello").size(), 5);
        assertEquals(ByteData.create(10, "UTF-8").write("안녕하세요").size(), 15);
    }
    
    @Test
    public void write() throws IOException {
        ByteData data = ByteData.create();
        data
            .write("hello".getBytes())
            .writeFillSpace(5)
            .writeFixed(1, 2, SPACE)
            .writeFixedAlignRight(2, 2, SPACE)
            .writeLine2()
            .write(Utils.zerofill(3, 2));
        
        assertEquals(data.toString(), "hello     1  2\r\n03");
    }
    
    @Test
    public void insert() throws IOException {
        ByteData data = ByteData.create();
        data
            .insert("hello".getBytes(), 0)
            .insertFillSpace(5, 5)
            .insertFixed("7", 2, SPACE, 3)
            .insertFixedAlignRight("8", 2, SPACE, 4)
            .rectifyWritePointer()
            .fillSpace();
        
        assertEquals(data.toString(), "hel7 8    ");
    }
    
    @Test
    public void read() throws IOException {
        ByteData data = ByteData.create().write("0001   23   0.0005.0hello          world");
        
        assertEquals(data.readTextInt(4, -1), 1);
        assertEquals(data.readTextInt(4, -1), 2);
        assertEquals(data.readTextInt(4, -1), 3);
        assertEquals(data.readTextInt(4, -1), 0);
        assertEquals(data.readTextLong(4, -1), 5L);
        assertEquals(data.readText(10, true), "hello");
        assertEquals(data.readTextAlignRight(10, true), "world");
        
        ByteData data2 = ByteData.create().write("0001   23   0.0005.0hello          world");
        assertThrows(Exception.class, () -> { data2.readTextLong(10, -1); });
        
        ByteData data3 = ByteData.create().write("0001   23   0.0005.0      \r\nhello          world");
        data3.readIgnoreCurrentLine();
        assertEquals(data3.readTextAlignRight(10, true), "hello     ");
        assertEquals(data3.readText(10, true), "     world");
    }
}
