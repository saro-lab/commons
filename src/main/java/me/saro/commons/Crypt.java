package me.saro.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.saro.commons.bytes.Bytes;

/**
 * Crypt
 * @author      PARK Yong Seo
 * @since       2.2
 */
public class Crypt {
    
    final Cipher cipher;
    final int mode;
    
    private Crypt(Cipher cipher, int mode) {
        this.cipher = cipher;
        this.mode = mode;
    }
    
    private static Crypt crypt(String transformation, int mode, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(mode, new SecretKeySpec(key, cipher.getAlgorithm()), new IvParameterSpec(iv));
        return new Crypt(cipher, mode);
    }
    
    public static Crypt encrypt(String transformation, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        return crypt(transformation, Cipher.ENCRYPT_MODE, key, iv);
    }
    
    public static Crypt decrypt(String transformation, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        return crypt(transformation, Cipher.DECRYPT_MODE, key, iv);
    }
    
    public void link(InputStream is, OutputStream os) throws IOException {
        try (CipherOutputStream cos = new CipherOutputStream(os, cipher)) {
            Utils.linkStream(is, cos);
        }
    }
    
    public byte[] toBytes(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(data, offset, length);
    }
    
    public byte[] toBytes(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(data);
    }
    
    public byte[] toBytesByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(Bytes.toBytesByHex(hex));
    }
    
    public byte[] toBytesByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(Base64.getDecoder().decode(base64));
    }
    
    public String toHex(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(toBytes(data, offset, length));
    }
    
    public String toHex(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(toBytes(data));
    }
    
    public String toHexByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(Bytes.toBytesByHex(hex));
    }
    
    public String toHexByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(Base64.getDecoder().decode(base64));
    }
    
    public String toBase64(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(toBytes(data, offset, length));
    }
    
    public String toBase64(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(toBytes(data));
    }
    
    public String toBase64ByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(Bytes.toBytesByHex(hex));
    }
    
    public String toBase64ByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(Base64.getDecoder().decode(base64));
    }
}
