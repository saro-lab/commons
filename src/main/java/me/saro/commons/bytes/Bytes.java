package me.saro.commons.bytes;

import java.nio.ByteBuffer;

/**
 * bytes
 * @author      PARK Yong Seo
 * @since       1.0
 */
public class Bytes {
    private Bytes() {
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
