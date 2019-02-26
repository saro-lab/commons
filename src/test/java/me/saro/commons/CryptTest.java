package me.saro.commons;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

import me.saro.commons.crypt.Crypt;

public class CryptTest {
    
    final static String CHARSET = "UTF-8";
    final static byte[] key = "d92liw93%1!9df0z".getBytes();
    final static byte[] iv = "12345qwert^%@!@f".getBytes();
    
    @Test
    public void cryptText() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        
        Crypt en = Crypt.encrypt("AES/CBC/PKCS5Padding", key, iv);
        Crypt de = Crypt.decrypt("AES/CBC/PKCS5Padding", key, iv);
        
        String text = "data";
        String encrypt = en.toHex(text.getBytes(CHARSET));
        String decrypt = new String(de.toBytesByHex(encrypt), CHARSET);
        
        assertEquals(text, decrypt);
        
        text = "data";
        encrypt = en.toBase64(text.getBytes(CHARSET));
        decrypt = new String(de.toBytesByBase64(encrypt), CHARSET);
        
        assertEquals(text, decrypt);
    }
    
//    @Test
//    public void cryptFile() throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
//        Crypt en = Crypt.encrypt("AES/CBC/PKCS5Padding", key, iv);
//        Crypt de = Crypt.decrypt("AES/CBC/PKCS5Padding", key, iv);
//        
//        File data = new File("C:/Users/SARO/Desktop/abc.jpg");
//        File encf = new File("C:/Users/SARO/Desktop/abc_en.jpg");
//        File decf = new File("C:/Users/SARO/Desktop/abc_de.jpg");
//        
//        en.to(data, encf, true);
//        de.to(encf, decf, true);
//    }
}
