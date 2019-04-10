package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.saro.commons.bytes.Bytes;
import me.saro.commons.bytes.FixedDataFormat;
import me.saro.commons.bytes.annotations.FixedBinary;
import me.saro.commons.bytes.annotations.FixedData;
import me.saro.commons.bytes.annotations.FixedText;
import me.saro.commons.bytes.annotations.FixedTextAlign;

public class FixedDataTest {

    @Test
    public void binary() {
        FixedDataFormat<BinaryStruct> format = FixedDataFormat.getInstance(BinaryStruct.class);
        
        BinaryStruct bs = new BinaryStruct((byte)-1, (short)321, 1234, 76543L, 2.1F, 3.6D, new byte[] {0x1f, 0x3b, 0x33});
        
        byte[] bytes = format.toBytes(bs);
        
        assertEquals(bytes.length, 30);
        
        System.out.println(Bytes.toHex(bytes));
        assertEquals(Bytes.toHex(bytes), "ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33");
        
        assertEquals(bs, format.toClass(bytes));
        
        byte[] bytes2 = new byte[60];
        format.bindBytes(bytes2, 0, bs);
        format.bindBytes(bytes2, 30, bs);
        
        System.out.println(Bytes.toHex(bytes2));
        assertEquals(Bytes.toHex(bytes2), "ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33ff0141000004d20000000000012aff40066666400ccccccccccccd1f3b33");
        
        assertEquals(bs, format.toClass(bytes2, 30));
    }
    
    @Test
    public void text() throws UnsupportedEncodingException {
        FixedDataFormat<TextStruct> format = FixedDataFormat.getInstance(TextStruct.class);
        
        TextStruct ts = new TextStruct((byte)-1/* -1 == 255 */, (short)-321, 32123, -21L, 12.3F, -342.5D, "가나다", "abc");
        
        byte[] bytes = format.toBytes(ts);
        assertEquals(bytes.length, 100);
        
        String text = new String(bytes, "UTF-8");
        System.out.println(text);
        assertEquals(text, "255-321   0000007d7b-21                 12.3                -342.5              가나다        abc");
        
        TextStruct ts2 = format.toClass(text.getBytes("UTF-8"));
        System.out.println(ts2);
        assertEquals(ts, ts2);
    }
    
    @Test
    public void mixed() {
        FixedDataFormat<MixedStruct> format = FixedDataFormat.getInstance(MixedStruct.class);
        MixedStruct ms = new MixedStruct("Yong Seo", "PARK", 1);
        
        byte[] bytes = format.toBytes(ms);
        
        assertEquals(bytes.length, 34);
        
        System.out.println(Bytes.toHex(bytes));
        assertEquals(Bytes.toHex(bytes), "596f6e672053656f202020202020205041524b202020202020202020202000000001");
        
        assertEquals(ms, format.toClass(bytes, 0));
        
        System.out.println(ms);
        System.out.println(format.toClass(bytes, 0));
    }
    
    @Test
    public void array() {
        FixedDataFormat<ArrayStruct> format = FixedDataFormat.create(ArrayStruct.class);
        ArrayStruct ms = new ArrayStruct(1, new int[] {2,3,4,5}, Arrays.asList(1L, -2L), new Short[] {21, 72});
        
        byte[] bytes = format.toBytes(ms);
        
        assertEquals(bytes.length, 40);
        
        System.out.println(Bytes.toHex(bytes));
        
        assertEquals(Bytes.toHex(bytes), "00000001000000020000000300000004000000050000000000000001fffffffffffffffe00150048");
        
        assertEquals(ms, format.toClass(bytes, 0));
        
        System.out.println(ms);
        System.out.println(format.toClass(bytes, 0));
    }
    
    @Data
    @FixedData(size=30, fill=0)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BinaryStruct {
        
        @FixedBinary(offset=0)
        byte byteData;
        
        @FixedBinary(offset=1)
        short shortData;
        
        @FixedBinary(offset=3)
        int intData;
        
        @FixedBinary(offset=7)
        Long longData; // test long -> Long
        
        @FixedBinary(offset=15)
        float floatData;
        
        @FixedBinary(offset=19)
        double doubleData;
        
        @FixedBinary(offset=27, arrayLength=3)
        byte[] bytesData;
    }
    
    @Data
    @FixedData(size=100, fill=0, charset="UTF-8")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextStruct {
        
        @FixedText(offset=0, length=3, unsigned=true)
        byte byteData;
        
        @FixedText(offset=3, length=7)
        Short shortData;
        
        @FixedText(offset=10, length=10, radix=16, fill='0', align=FixedTextAlign.right)
        int intData;
        
        @FixedText(offset=20, length=20)
        long longData;
        
        @FixedText(offset=40, length=20)
        float floatData;
        
        @FixedText(offset=60, length=20)
        double doubleData;
        
        @FixedText(offset=80, length=10)
        String leftText;
        
        @FixedText(offset=90, length=10, align=FixedTextAlign.right)
        String rightText;
    }
    
    @Data
    @FixedData(size=34, charset="UTF-8")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MixedStruct {
        
        @FixedText(offset=0, length=15)
        String firstName;
        
        @FixedText(offset=15, length=15)
        String lastName;
        
        @FixedBinary(offset=30)
        int memberId;
    }
    
    @Data
    @FixedData(size=40)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ArrayStruct {
        
        @FixedBinary(offset=0)
        int member1;
        
        @FixedBinary(offset=4, arrayLength=4)
        int[] member2;
        
        @FixedBinary(offset=20, arrayLength=2)
        List<Long> member3;
        
        @FixedBinary(offset=36, arrayLength=2)
        Short[] member4;
    }
}
