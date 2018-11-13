package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.saro.commons.bytes.Bytes;
import me.saro.commons.bytes.FixedDataFormat;
import me.saro.commons.bytes.annotations.BinaryData;
import me.saro.commons.bytes.annotations.FixedData;

public class FixedDataTest {

    @Test
    public void binary() {
        FixedDataFormat<BinaryStruct> format = FixedDataFormat.create(BinaryStruct.class, () -> new BinaryStruct());
        
        BinaryStruct bs = new BinaryStruct((byte)-1, (short)321, 1234, 76543L, 2.1F, 3.6D, new byte[] {0x1f, 0x3b, 0x33});
        
        byte[] bytes = format.toBytes(bs);
        
        assertEquals(bytes.length, 30);
        
        assertEquals(Bytes.toHex(bytes), "ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33");
        
        assertEquals(bs, format.toClass(bytes));
        
        
        byte[] bytes2 = new byte[60];
        format.bindBytes(bytes2, 0, bs);
        format.bindBytes(bytes2, 30, bs);
        
        assertEquals(Bytes.toHex(bytes2), "ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33");
        
        assertEquals(bs, format.toClass(bytes2, 30));
    }
    
//    @Test
//    public void text() {
//       
//    }
//    
//    @Test
//    public void mixed() {
//       
//    }
    
    @Data
    @FixedData(size=30, fill=0)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BinaryStruct {
        
        @BinaryData(offset=0)
        byte byteData;
        
        @BinaryData(offset=1)
        short shortData;
        
        @BinaryData(offset=3)
        int intData;
        
        @BinaryData(offset=7)
        long longData;
        
        @BinaryData(offset=15)
        float floatData;
        
        @BinaryData(offset=19)
        double doubleData;
        
        @BinaryData(offset=27, arrayLength=3)
        byte[] bytesData;
    }
}
