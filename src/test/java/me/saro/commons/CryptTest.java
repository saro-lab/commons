package me.saro.commons;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

public class CryptTest {
    @Test
    public void crypt() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        
//        String charset = "UTF-8";
//        
//        String trens = "AES/CBC/PKCS5Padding";
//        Key key = new SecretKeySpec("abcdefg123456789".getBytes(), "AES");
//        byte[] iv = ")3ksu38s6Ad8D2^&".getBytes();
//        
//        String test = "abcdefg 가나다라 @#$!ㅇㄴ8732!$%";
//        
//        String en = Bytes.toHex(Converter.encrypt(trens, key, iv, test.getBytes(charset)));
//        String de = new String(Converter.decrypt(trens, key, iv, Bytes.toBytesByHex(en)), charset);
//        
//        assertEquals(test, de);
    }
}
