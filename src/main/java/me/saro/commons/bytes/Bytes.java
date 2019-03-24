package me.saro.commons.bytes;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.stream.IntStream;

import lombok.SneakyThrows;

/**
 * bytes
 * @author      PARK Yong Seo
 * @since       1.0
 */
public class Bytes {
    
    private Bytes() {
    }
    
    final static Encoder EN_BASE64 = Base64.getEncoder();
    final static Decoder DE_BASE64 = Base64.getDecoder();
    
    // HAX convert array [00 ~ FF]
    final static char[][] BYTE_TO_HEX_STR_MAP = IntStream.range(0, 256).boxed()
            .map(i -> String.format("%02x", i).toCharArray()).toArray(char[][]::new);
    
    /**
     * bytes to hex
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        StringBuilder rv = new StringBuilder((bytes.length * 2) + 10);
        for (byte b : bytes) {
            rv.append(BYTE_TO_HEX_STR_MAP[((int) b) & 0xff]);
        }
        return rv.toString();
    }
    
    /**
     * to bytes by hex string
     * @param hex
     * @return
     */
    public static byte[] toBytesByHex(String hex) {
        byte[] rv = new byte[hex.length() / 2];
        int rvp = 0;
        for (int i = 0 ; i < hex.length() ; i+=2) {
            rv[rvp++] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
        }
        return rv;
    }
    
    /**
     * byte data to base64String
     * @param data
     * @return
     */
    public static String encodeBase64String(byte[] data) {
        return EN_BASE64.encodeToString(data);
    }
    
    /**
     * text to base64String
     * @param data
     * @param charset
     * @return
     */
    @SneakyThrows
    public static String encodeBase64String(String text, String charset) {
        return EN_BASE64.encodeToString(text.getBytes(charset));
    }
    
    /**
     * base64String to byte data
     * @param base64
     * @return
     */
    public static byte[] decodeBase64(String base64) {
        return DE_BASE64.decode(base64);
    }
    
    /**
     * base64 to text
     * @param base64
     * @param charset
     * @return
     */
    @SneakyThrows
    public static String decodeBase64(String base64, String charset) {
        return new String(DE_BASE64.decode(base64), charset);
    }
    
    /**
     * short to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(short val) {
        return ByteBuffer.allocate(2).putShort(val).array();
    }
    
    /**
     * int to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }
    
    /**
     * long to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(long val) {
        return ByteBuffer.allocate(8).putLong(val).array();
    }
    
    /**
     * float to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(float val) {
        return ByteBuffer.allocate(4).putFloat(val).array();
    }
    
    /**
     * double to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(double val) {
        return ByteBuffer.allocate(8).putDouble(val).array();
    }
    
    /**
     * bytes to short
     * @param val
     * @param offset
     * @return
     */
    public static short toShort(byte[] val, int offset) {
        return ByteBuffer.wrap(val, offset, 2).getShort();
    }
    
    /**
     * bytes to short
     * @param val
     * @return
     */
    public static short toShort(byte[] val) {
        return ByteBuffer.wrap(val, 0, 2).getShort();
    }
    
    /**
     * bytes to int
     * @param val
     * @param offset
     * @return
     */
    public static int toInt(byte[] val, int offset) {
        return ByteBuffer.wrap(val, offset, 4).getInt();
    }
    
    /**
     * bytes to int
     * @param val
     * @return
     */
    public static int toInt(byte[] val) {
        return ByteBuffer.wrap(val, 0, 4).getInt();
    }
    
    /**
     * bytes to long
     * @param val
     * @param offset
     * @return
     */
    public static long toLong(byte[] val, int offset) {
        return ByteBuffer.wrap(val, offset, 8).getLong();
    }
    
    /**
     * bytes to long
     * @param val
     * @return
     */
    public static long toLong(byte[] val) {
        return ByteBuffer.wrap(val, 0, 8).getLong();
    }
    
    /**
     * bytes to float
     * @param val
     * @param offset
     * @return
     */
    public static float toFloat(byte[] val, int offset) {
        return ByteBuffer.wrap(val, offset, 4).getFloat();
    }
    
    /**
     * bytes to float
     * @param val
     * @return
     */
    public static float toFloat(byte[] val) {
        return ByteBuffer.wrap(val, 0, 4).getFloat();
    }
    
    /**
     * bytes to double
     * @param val
     * @param offset
     * @return
     */
    public static double toDouble(byte[] val, int offset) {
        return ByteBuffer.wrap(val, offset, 8).getDouble();
    }
    
    /**
     * bytes to double
     * @param val
     * @return
     */
    public static double toDouble(byte[] val) {
        return ByteBuffer.wrap(val, 0, 8).getDouble();
    }
}
