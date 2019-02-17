package me.saro.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
 * thread-safe
 * @author      PARK Yong Seo
 * @since       2.2
 */
public class Crypt {
    
    final private Integer LOCK = 1;
    final private Cipher cipher;
    //final private int mode;
    
    private Crypt(Cipher cipher, int mode) {
        this.cipher = cipher;
        //this.mode = mode;
    }
    
    private static Crypt crypt(String transformation, int mode, byte[] key, byte[] iv) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(mode, new SecretKeySpec(key, cipher.getAlgorithm().split("\\/")[0]), new IvParameterSpec(iv));
        return new Crypt(cipher, mode);
    }
    
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
        return crypt(transformation, Cipher.ENCRYPT_MODE, key, iv);
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
        return crypt(transformation, Cipher.DECRYPT_MODE, key, iv);
    }
    
    /**
     * input -> (en/de)crypt -> output
     * @param is
     * @param os
     * @throws IOException
     */
    public void to(InputStream is, OutputStream os) throws IOException {
        synchronized (LOCK) {
            try (CipherOutputStream cos = new CipherOutputStream(os, cipher)) {
                Utils.linkStream(is, cos);
            }
        }
    }
    
    /**
     * input file -> (en/de)crypt -> output file
     * @param in
     * @param out
     * @param overwrite the exist file
     * @throws IOException
     */
    public void to(File in, File out, boolean overwrite) throws IOException {
        synchronized (LOCK) {
            if (!in.exists()) {
                throw new IOException(in.getAbsolutePath() + " does not exist");
            }
            if (out.exists()) {
                if (overwrite) {
                    out.delete();
                } else {
                    throw new IOException(in.getAbsolutePath() + " is already exist");
                }
            }
            out.getParentFile().mkdirs();
            try (FileInputStream fis = new FileInputStream(in) ; FileOutputStream fos = new FileOutputStream(out) ; CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                Utils.linkStream(fis, cos);
            }
        }
    }
    
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
    public byte[] toBytes(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
        byte[] rv;
        synchronized (LOCK) {
            rv = cipher.doFinal(data, offset, length);
        }
        return rv;
    }
    
    /**
     * to byte<br>
     * input bytes -> (en/de)crypt -> output bytes
     * @param data
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] toBytes(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
        byte[] rv;
        synchronized (LOCK) {
            rv = cipher.doFinal(data);
        }
        return rv;
    }
    
    /**
     * to byte<br>
     * input hex string -> (en/de)crypt -> output bytes
     * @param hex
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] toBytesByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
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
    public byte[] toBytesByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toHex(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toHex(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toHexByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toHexByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toBase64(byte[] data, int offset, int length) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toBase64(byte[] data) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toBase64ByHex(String hex) throws IllegalBlockSizeException, BadPaddingException {
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
    public String toBase64ByBase64(String base64) throws IllegalBlockSizeException, BadPaddingException {
        return Base64.getEncoder().encodeToString(Base64.getDecoder().decode(base64));
    }
}
