package me.saro.commons;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * Crypt
 * @author      PARK Yong Seo
 * @since       2.2
 */
public class Crypt {    
    /**
     * simple crypt
     * @param cryptMode
     * @param transformation
     * @param key
     * @param iv
     * @param in
     * @return
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static byte[] crypt(int cryptMode, String transformation, Key key, byte[] iv, byte[] in)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(cryptMode, key, new IvParameterSpec(iv));
        return cipher.doFinal(in);
    }

    /**
     * simple encrypt
     * @param transformation
     * @param key
     * @param iv
     * @param in
     * @return
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] encrypt(String transformation, Key key, byte[] iv, byte[] in)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return crypt(Cipher.ENCRYPT_MODE, transformation, key, iv, in);
    }

    /**
     * simple decrypt
     * @param transformation
     * @param key
     * @param iv
     * @param in
     * @return
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] decrypt(String transformation, Key key, byte[] iv, byte[] in)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return crypt(Cipher.DECRYPT_MODE, transformation, key, iv, in);
    }
}
