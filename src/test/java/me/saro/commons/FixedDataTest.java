package me.saro.commons;

import org.junit.jupiter.api.Test;

import lombok.Data;
import me.saro.commons.bytes.FixedDataFormat;
import me.saro.commons.bytes.annotations.FixedData;
import me.saro.commons.bytes.annotations.TextData;
import me.saro.commons.bytes.annotations.TextDataAlign;

public class FixedDataTest {

    @Test
    public void test() {
        
        try {
            fixeddata();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void fixeddata() throws Exception {
        FixedDataFormat<Abc> fd = FixedDataFormat.create(Abc.class);
        byte[] aaa = "12345                                             ".getBytes();
        
        System.out.println(fd.toClass(aaa));
        
        Tests.timestamp(() -> {
          for (int i = 0 ; i < 10000 ; i++) {
              fd.toClass(aaa);
          }
        });
    }

    
    @Data @FixedData(size=200)
    public static class Abc {
        @TextData(offset=0, length=10, align=TextDataAlign.right)
        String c1;
        
        @TextData(offset=0, length=10)
        String a1;
        
        
        @TextData(offset=0, length=10)
        byte a2;
        
        @TextData(offset=0, length=10)
        short a3;
        
        @TextData(offset=0, length=10)
        int a4;
        
        @TextData(offset=0, length=10)
        long a5;
        
        @TextData(offset=0, length=10)
        float a6;
        
        @TextData(offset=0, length=10)
        double a7;
        
        @TextData(offset=0, length=10)
        Byte a12;
        
        @TextData(offset=0, length=10)
        Short a13;
        
        @TextData(offset=0, length=10)
        Integer a14;
        
        @TextData(offset=0, length=10)
        Long a15;
        
        @TextData(offset=0, length=10)
        Float a16;
        
        @TextData(offset=0, length=10)
        Double a17;
        
        String b1;
    }
}
