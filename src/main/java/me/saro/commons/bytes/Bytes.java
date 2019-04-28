package me.saro.commons.bytes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
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
     * short[] to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(short[] val) {
        return toBytes(val, 0, val.length);
    }
    
    /**
     * short[] to bytes
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static byte[] toBytes(short[] val, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(2 * (length - offset));
        for (int i = offset ; i < (offset + length) ; i++) {
            bb.putShort(val[i]);
        }
        return bb.array();
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
     * int[] to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(int[] val) {
        return toBytes(val, 0, val.length);
    }
    
    /**
     * int[] to bytes
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static byte[] toBytes(int[] val, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(4 * (length - offset));
        for (int i = offset ; i < (offset + length) ; i++) {
            bb.putInt(val[i]);
        }
        return bb.array();
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
     * long[] to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(long[] val) {
        return toBytes(val, 0, val.length);
    }
    
    /**
     * long[] to bytes
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static byte[] toBytes(long[] val, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(8 * (length - offset));
        for (int i = offset ; i < (offset + length) ; i++) {
            bb.putLong(val[i]);
        }
        return bb.array();
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
     * float[] to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(float[] val) {
        return toBytes(val, 0, val.length);
    }
    
    /**
     * float[] to bytes
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static byte[] toBytes(float[] val, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(4 * (length - offset));
        for (int i = offset ; i < (offset + length) ; i++) {
            bb.putFloat(val[i]);
        }
        return bb.array();
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
     * double[] to bytes
     * @param val
     * @return
     */
    public static byte[] toBytes(double[] val) {
        return toBytes(val, 0, val.length);
    }
    
    /**
     * double[] to bytes
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static byte[] toBytes(double[] val, int offset, int length) {
        ByteBuffer bb = ByteBuffer.allocate(8 * (length - offset));
        for (int i = offset ; i < (offset + length) ; i++) {
            bb.putDouble(val[i]);
        }
        return bb.array();
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
     * bytes to short[]
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static short[] toShortArray(byte[] bytes, int bytesOffset, int arrayLength) {
        short[] rv = new short[arrayLength];
        for (int i = 0 ; i < arrayLength ; i++) {
            rv[i] = ByteBuffer.wrap(bytes, bytesOffset + (2 * i), 2).getShort();
        }
        return rv;
    }
    
    /**
     * bytes to short list
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static List<Short> toShortList(byte[] bytes, int bytesOffset, int arrayLength) {
        List<Short> rv = new ArrayList<>();
        for (int i = 0 ; i < arrayLength ; i++) {
            rv.add(ByteBuffer.wrap(bytes, bytesOffset + (2 * i), 2).getShort());
        }
        return rv;
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
     * bytes to int[]
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static int[] toIntArray(byte[] bytes, int bytesOffset, int arrayLength) {
        int[] rv = new int[arrayLength];
        for (int i = 0 ; i < arrayLength ; i++) {
            rv[i] = ByteBuffer.wrap(bytes, bytesOffset + (4 * i), 4).getInt();
        }
        return rv;
    }
    
    /**
     * bytes to integer list
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static List<Integer> toIntegerList(byte[] bytes, int bytesOffset, int arrayLength) {
        List<Integer> rv = new ArrayList<>();
        for (int i = 0 ; i < arrayLength ; i++) {
            rv.add(ByteBuffer.wrap(bytes, bytesOffset + (4 * i), 4).getInt());
        }
        return rv;
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
     * bytes to long[]
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static long[] toLongArray(byte[] bytes, int bytesOffset, int arrayLength) {
        long[] rv = new long[arrayLength];
        for (int i = 0 ; i < arrayLength ; i++) {
            rv[i] = ByteBuffer.wrap(bytes, bytesOffset + (8 * i), 8).getLong();
        }
        return rv;
    }
    
    /**
     * bytes to long list
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static List<Long> toLongList(byte[] bytes, int bytesOffset, int arrayLength) {
        List<Long> rv = new ArrayList<>();
        for (int i = 0 ; i < arrayLength ; i++) {
            rv.add(ByteBuffer.wrap(bytes, bytesOffset + (8 * i), 8).getLong());
        }
        return rv;
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
     * bytes to float[]
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static float[] toFloatArray(byte[] bytes, int bytesOffset, int arrayLength) {
        float[] rv = new float[arrayLength];
        for (int i = 0 ; i < arrayLength ; i++) {
            rv[i] = ByteBuffer.wrap(bytes, bytesOffset + (4 * i), 4).getFloat();
        }
        return rv;
    }
    
    /**
     * bytes to float list
     * @param val
     * @param offset
     * @param arrayLength
     * @return
     */
    public static List<Float> toFloatList(byte[] bytes, int bytesOffset, int arrayLength) {
        List<Float> rv = new ArrayList<>();
        for (int i = 0 ; i < arrayLength ; i++) {
            rv.add(ByteBuffer.wrap(bytes, bytesOffset + (4 * i), 4).getFloat());
        }
        return rv;
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
     * bytes to double[]
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static double[] toDoubleArray(byte[] bytes, int bytesOffset, int arrayLength) {
        double[] rv = new double[arrayLength];
        for (int i = 0 ; i < arrayLength ; i++) {
            rv[i] = ByteBuffer.wrap(bytes, bytesOffset + (8 * i), 8).getDouble();
        }
        return rv;
    }
    
    /**
     * bytes to double list
     * @param val
     * @param offset
     * @param length
     * @return
     */
    public static List<Double> toDoubleList(byte[] bytes, int bytesOffset, int arrayLength) {
        List<Double> rv = new ArrayList<>();
        for (int i = 0 ; i < arrayLength ; i++) {
            rv.add(ByteBuffer.wrap(bytes, bytesOffset + (8 * i), 8).getDouble());
        }
        return rv;
    }
    
    /**
     * bytes to double
     * @param val
     * @return
     */
    public static double toDouble(byte[] val) {
        return ByteBuffer.wrap(val, 0, 8).getDouble();
    }
    
    /**
     * reverse
     * @param data
     * @param reverse true : do reverse, false : n/a
     */
    public static byte[] copy(byte[] data, boolean reverse) {
        if (reverse) {
            return reverse(Arrays.copyOf(data, data.length));
        } else {
            return Arrays.copyOf(data, data.length);
        }
    }
    
    /**
     * reverse
     * @param data
     * @param reverse true : do reverse, false : just copy
     */
    public static byte[] copy(byte[] data, int offset, int length, boolean reverse) {
        if (reverse) {
            return reverse(Arrays.copyOfRange(data, offset, length));
        } else {
            return Arrays.copyOfRange(data, offset, length);
        }
    }
    
    /**
     * reverse
     * @param data
     * @param reverse true : do reverse, false : n/a
     */
    public static byte[] reverse(byte[] data, boolean reverse) {
        if (reverse) {
            return reverse(data);
        }
        return data;
    }
    
    /**
     * reverse
     * @param data
     * @param reverse true : do reverse, false : n/a
     */
    public static byte[] reverse(byte[] data, int offset, int length, boolean reverse) {
        if (reverse) {
            return reverse(data, offset, length);
        }
        return data;
    }
    
    /**
     * reverse byte
     * @param data
     */
    public static byte[] reverse(byte[] data) {
        return reverse(data, 0, data.length);
    }
    
    /**
     * reverse byte
     * offset to length
     * @param data
     * @param offset
     * @param length
     */
    public static byte[] reverse(byte[] data, int offset, int length) {
        if (length < 1) {
            throw new IllegalArgumentException("length must much more then 0");
        } else if (length == 1) {
            return data;
        }
        byte t;
        int move = length / 2;
        for (int i = 0, s = offset, e = length - 1 ; i < move ; i++, s++, e--) {
            t = data[s];
            data[s] = data[e];
            data[e] = t;
        }
        return data;
    }
    
    /**
     * reverse byte
     * offset to (length unit) * arrayLength
     * @param data
     * @param offset
     * @param length
     * @param arrayLength
     */
    public static byte[] reverse(byte[] data, int offset, int length, int arrayLength) {
        if (arrayLength < 1) {
            throw new IllegalArgumentException("arrayLength must much more then 0");
        }
        for (int i = 0 ; i < arrayLength ; i++) {
            reverse(data, offset + (i * length), length);
        }
        return data;
    }
}
