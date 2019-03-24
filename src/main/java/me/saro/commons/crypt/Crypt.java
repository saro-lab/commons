package me.saro.commons.crypt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.saro.commons.bytes.Bytes;

/**
 * Crypt
 * thread-safe
 * @author      PARK Yong Seo
 * @since       3.0
 */
public interface Crypt {
    
    /**
     * encrypt
     * @param transformation ex) AES/CBC/PKCS5Padding
     * @param key key
     * @param iv iv
     * @return
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public static Crypt encrypt(String transformation, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, cipher.getAlgorithm().split("\\/")[0]), new IvParameterSpec(iv));
        return new SimpleCrypt(cipher);
    }
    
    /**
     * decrypt
     * @param transformation ex) AES/CBC/PKCS5Padding
     * @param key key
     * @param iv iv
     * @return
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public static Crypt decrypt(String transformation, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, cipher.getAlgorithm().split("\\/")[0]), new IvParameterSpec(iv));
        return new SimpleCrypt(cipher);
    }
    
    /**
     * input -> (en/de)crypt -> output
     * @param is
     * @param os
     * @throws IOException
     */
    public void to(InputStream is, OutputStream os) throws IOException;
    
    /**
     * input file -> (en/de)crypt -> output file
     * @param in
     * @param out
     * @param overwrite the exist file
     * @throws IOException
     */
    public void to(File in, File out, boolean overwrite) throws IOException;
    
    /**
     * to byte<br>
     * input bytes -> (en/de)crypt -> output bytes
     * @param data
     * @param offset
     * @param length
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] toBytes(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException;
    
    /**
     * to byte<br>
     * input bytes -> (en/de)crypt -> output bytes
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] toBytes(byte[] data) throws IllegalBlockSizeException, BadPaddingException;
    
    /**
     * to byte<br>
     * input hex string -> (en/de)crypt -> output bytes
     * @param hex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public byte[] toBytesByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
        return toBytes(Bytes.toBytesByHex(hex));
    }
    
    /**
     * to byte<br>
     * input base64 string -> (en/de)crypt -> output bytes
     * @param base64
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public byte[] toBytesByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return toBytes(Base64.getDecoder().decode(base64));
    }
    
    /**
     * to hex string<br>
     * input bytes -> (en/de)crypt -> output hex string
     * @param data
     * @param offset
     * @param length
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toHex(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(toBytes(data, offset, length));
    }
    
    /**
     * to hex string<br>
     * input bytes -> (en/de)crypt -> output hex string
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toHex(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(toBytes(data));
    }
    
    /**
     * to hex string<br>
     * input hex string -> (en/de)crypt -> output hex string
     * @param hex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toHexByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(Bytes.toBytesByHex(hex));
    }
    
    /**
     * to hex string<br>
     * input base64 string -> (en/de)crypt -> output hex string
     * @param base64
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toHexByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return Bytes.toHex(Base64.getDecoder().decode(base64));
    }
    
    /**
     * to base64 string<br>
     * input bytes -> (en/de)crypt -> output base64 string
     * @param data
     * @param offset
     * @param length
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toBase64(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(toBytes(data, offset, length));
    }
    
    /**
     * to base64 string<br>
     * input bytes -> (en/de)crypt -> output base64 string
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toBase64(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(toBytes(data));
    }
    
    /**
     * to base64 string<br>
     * input hex string -> (en/de)crypt -> output base64 string
     * @param hex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toBase64ByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(Bytes.toBytesByHex(hex));
    }
    
    /**
     * to base64 string<br>
     * input base64 string -> (en/de)crypt -> output base64 string
     * @param base64
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    default public String toBase64ByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(Base64.getDecoder().decode(base64));
    }
}
