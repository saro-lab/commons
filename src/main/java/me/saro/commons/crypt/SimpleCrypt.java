package me.saro.commons.crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;

import me.saro.commons.Utils;

/**
 * Crypt
 * thread-safe
 * @author      PARK Yong Seo
 * @since       2.2
 */
public class SimpleCrypt implements Crypt {
    
    final private Integer LOCK = 1;
    final private Cipher cipher;
    
    SimpleCrypt(Cipher cipher) {
        this.cipher = cipher;
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
    
}
