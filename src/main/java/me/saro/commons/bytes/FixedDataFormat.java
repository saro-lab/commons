package me.saro.commons.bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import me.saro.commons.bytes.annotations.BinaryData;
import me.saro.commons.bytes.annotations.FixedData;
import me.saro.commons.bytes.annotations.TextData;
import me.saro.commons.bytes.annotations.TextDataAlign;
import me.saro.commons.function.ThrowableConsumer;
import me.saro.commons.function.ThrowableSupplier;

/**
 * DataFormat 
 * @author      PARK Yong Seo
 * @since       1.0
 */
public class FixedDataFormat<T> {
    
    final Class<T> clazz;
    final FixedData fixedData;
    final Supplier<T> newInstance;
    final List<FixedDataBytesToClass> toClassOrders = Collections.synchronizedList(new ArrayList<>());
    final List<FixedDataClassToBytes> toBytesOrders = Collections.synchronizedList(new ArrayList<>());
    
    // private
    private FixedDataFormat(Class<T> clazz, Supplier<T> newInstance) {
        this.clazz = clazz;
        fixedData = clazz.getDeclaredAnnotation(FixedData.class);
        this.newInstance = newInstance;
    }
    
    /**
     * create DataFormat
     * @param clazz
     * @param newInstance
     * @return
     */
    public static <T> FixedDataFormat<T> create(Class<T> clazz, Supplier<T> newInstance) {
        return new FixedDataFormat<>(clazz, newInstance).init();
    }
    
    /**
     * create DataFormat<br>
     * user defualt constructor
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> FixedDataFormat<T> create(Class<T> clazz) {
        return create(clazz, ThrowableSupplier.runtime(() -> (T)clazz.getDeclaredConstructors()[0].newInstance()));
    }
    
    public T toClass(byte[] bytes, int offset) {
        T t = newInstance.get();
        toClassOrders.parallelStream().forEach(ThrowableConsumer.runtime(e -> e.order(t, bytes, offset)));
        return t;
    }
    
    public T toClass(byte[] bytes) {
        return toClass(bytes, 0);
    }
    
    /**
     * 
     * @param bytes
     * @return
     */
    public T toClassWithCheckSize(byte[] bytes) {
        if (bytes.length != fixedData.size()) {
            throw new IllegalArgumentException("size not matched define["+fixedData.size()+"] data[]"+bytes.length+"");
        }
        return toClass(bytes, 0);
    }
    
    /**
     * 
     * @param line
     * @return
     */
    public T toClassWithCheckSize(String line) {
        return toClassWithCheckSize(line, fixedData.charset());
    }
    
    /**
     * 
     * @param line
     * @param charset
     * @return
     */
    public T toClassWithCheckSize(String line, String charset) {
        try {
            return toClassWithCheckSize(line.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void bindBytes(byte[] outputBytes, int offset, T obj) {
        Arrays.fill(outputBytes, offset, offset + fixedData.size(), fixedData.fill());
        toBytesOrders.parallelStream().forEach(ThrowableConsumer.runtime(e -> e.order(obj, outputBytes, offset)));
    }
    
    public void bindBytes(OutputStream out, T obj) throws IOException {
        byte[] buf = new byte[fixedData.size()];
        bindBytes(buf, 0, obj);
        out.write(buf);
    }
    
    public byte[] toBytes(T obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bindBytes(baos, obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * init fixed data format
     * @return
     */
    private FixedDataFormat<T> init() {
        
        if (fixedData == null) {
            throw new IllegalArgumentException(clazz.getName() + " need to Declared @FixedData Annotation");
        }
        if (fixedData.size() < 1) {
            throw new IllegalArgumentException(clazz.getName() + " need to Declared size");
        }
        
        Stream.of(clazz.getDeclaredFields()).parallel().forEach(ThrowableConsumer.runtime(field -> {
            field.setAccessible(true);
            BinaryData binary = field.getDeclaredAnnotation(BinaryData.class);
            TextData text = field.getDeclaredAnnotation(TextData.class);
            if (binary != null) {
                bindToClassOrder(field, binary);
                bindToBytesOrder(field, binary);
            } else if (text != null) {
                bindToClassOrder(field, text);
                bindToBytesOrder(field, text);
            }
        }));
        return this;
    }
    
    /**
     * to bytes by field value
     * @param field
     * @param da
     */
    private void bindToBytesOrder(Field field, BinaryData da) {
        int dfOffset = da.offset();
        int arrayLength = da.arrayLength();
        String type = field.getType().getName();
        
        toBytesOrders.add((clazz, bytes, offset) -> {
            
            int s = offset + dfOffset;
            Object val = field.get(clazz);
            
            if (val == null) {
                return;
            }
            
            switch (type) {
                case "[B" : case "[Ljava.lang.Byte;" :
                    System.arraycopy(val, 0, bytes, s, arrayLength);
                return;
                case "byte" : case "java.lang.Byte" :
                    bytes[s] = (byte)val;
                return;
                case "short" : case "java.lang.Short" :
                    System.arraycopy(Bytes.toBytes((short)val), 0, bytes, s, 2);
                return;
                case "int" : case "java.lang.Integer" :
                    System.arraycopy(Bytes.toBytes((int)val), 0, bytes, s, 4);
                return;
                case "long" : case "java.lang.Long" : 
                    System.arraycopy(Bytes.toBytes((long)val), 0, bytes, s, 8);
                return;
                case "float" : case "java.lang.Float" : 
                    System.arraycopy(Bytes.toBytes((float)val), 0, bytes, s, 4);
                return;
                case "double" : case "java.lang.Double" : 
                    System.arraycopy(Bytes.toBytes((double)val), 0, bytes, s, 8);
                return;
                default : 
                    throw new IllegalArgumentException("type ["+type+"] does not support");
            }
        });
    }
    
    /**
     * to bytes by field value
     * @param field
     * @param da
     */
    private void bindToBytesOrder(Field field, TextData da) {
        int dfOffset = da.offset();
        int dfLength = da.length();
        boolean isLeft = da.align() == TextDataAlign.left;
        String type = field.getType().getName();
        boolean unsigned = da.unsigned();
        String charset = "".equals(da.charset()) ? fixedData.charset() : da.charset();
        int radix = da.radix();
        
        toBytesOrders.add((clazz, bytes, offset) -> {
            int s = offset + dfOffset;
            byte fill = da.fill();
            Object val = field.get(clazz);
            byte[] vbytes;
            
            if (val == null) {
                int e = s + dfLength;
                for (; s < e ; s++) {
                    bytes[s] = fill;
                }
                return;
            }
            
            convert : switch (type) {
                case "java.lang.String" :
                    vbytes = ((String)val).getBytes(charset);
                break convert;
                case "byte" : case "java.lang.Byte" :
                    vbytes = Integer.toString(unsigned ? Byte.toUnsignedInt((byte)val) : (int)(byte)val, radix).getBytes();
                break convert;
                case "short" : case "java.lang.Short" :
                    vbytes = Integer.toString(unsigned ? Short.toUnsignedInt((short)val) : (int)(short)val, radix).getBytes();
                break convert;
                case "int" : case "java.lang.Integer" :
                    vbytes = (unsigned ? Integer.toUnsignedString((int)val, radix) : Integer.toString((int)val, radix)).getBytes();
                break convert;
                case "long" : case "java.lang.Long" : 
                    vbytes = (unsigned ? Long.toUnsignedString((long)val, radix) : Long.toString((long)val, radix)).getBytes();
                break convert;
                case "float" : case "java.lang.Float" : 
                    vbytes = (Float.toString((float)val)).getBytes();
                break convert;
                case "double" : case "java.lang.Double" : 
                    vbytes = (Double.toString((double)val)).getBytes();
                break convert;
                default : 
                    throw new IllegalArgumentException("type ["+type+"] does not support");
            }
            
            int vLen = vbytes.length;
            if (vLen > dfLength) {
                throw new IllegalArgumentException("["+new String(vbytes, charset)+"] is out of range of "+field.getName());
            } else if (isLeft) {
                System.arraycopy(vbytes, 0, bytes, s, vLen);
                for (int e = s + dfLength, i = (s + vLen) ; i < e ; i++) {
                    bytes[i] = fill;
                }
            } else {
                int e = s + (dfLength - vLen);
                for (; s < e ; s++) {
                    bytes[s] = fill;
                }
                System.arraycopy(vbytes, 0, bytes, s, vLen);
            }
        });
    }
    
    /**
     * to class by binary data
     * @param field
     * @param da
     */
    private void bindToClassOrder(Field field, BinaryData da) {
        int dfOffset = da.offset();
        int arrayLength = da.arrayLength();
        String type = field.getType().getName();
        
        if (type.startsWith("[") && arrayLength < 1) {
            throw new IllegalArgumentException("arrayLength must over then 1");
        }
        
        toClassOrders.add((obj, bytes, offset) -> {
            int s = offset + dfOffset;
            
            switch (type) {
                case "[B" : case "[Ljava.lang.Byte;" :
                    field.set(obj, Arrays.copyOfRange(bytes, s, s + arrayLength));
                return;
                case "byte" : case "java.lang.Byte" :
                    field.set(obj, bytes[s]);
                return;
                case "short" : case "java.lang.Short" :
                    field.set(obj, Bytes.toShort(bytes, s));
                return;
                case "int" : case "java.lang.Integer" :
                    field.set(obj, Bytes.toInt(bytes, s));
                return;
                case "long" : case "java.lang.Long" : 
                    field.set(obj, Bytes.toLong(bytes, s));
                return;
                case "float" : case "java.lang.Float" : 
                    field.set(obj, Bytes.toFloat(bytes, s));
                return;
                case "double" : case "java.lang.Double" : 
                    field.set(obj, Bytes.toDouble(bytes, s));
                return;
                default : 
                    throw new IllegalArgumentException("type ["+type+"] does not support");
            }
        });
    }
    
    /**
     * to class by text data
     * @param field
     * @param da
     */
    private void bindToClassOrder(Field field, TextData da) {
        int dfOffset = da.offset();
        int dfLength = da.length();
        byte dfFill = da.fill();
        boolean isLeft = da.align() == TextDataAlign.left;
        String type = field.getType().getName();
        boolean unsigned = da.unsigned();
        String charset = "".equals(da.charset()) ? fixedData.charset() : da.charset();
        int radix = da.radix();
        
        if (dfLength < 1) {
            throw new IllegalArgumentException("length must over then 1");
        }
        
        toClassOrders.add((obj, bytes, offset) -> {
            int s = offset + dfOffset;
            int e = s + dfLength;
            byte fill = dfFill;
            String val;
            stop : if (isLeft) {
                while (s < e) {
                    if (bytes[--e] != fill) {
                        val = new String(bytes, s, e - s + 1, charset);
                        break stop;
                    }
                }
                val = "";
            } else {
                while (s < e) {
                    if (bytes[s] != fill) {
                        val = new String(bytes, s, e - s, charset);
                        break stop;
                    }
                    s++;
                }
                val = "";
            }
            
            if ("java.lang.String".equals(type)) {
                field.set(obj, val);
            } else {
                try {
                    switch (type) {
                        case "byte" : case "java.lang.Byte" :
                            field.set(obj, (byte)Integer.parseInt(val, radix));
                        return;
                        case "short" : case "java.lang.Short" :
                            field.set(obj, (short)Integer.parseInt(val, radix));
                        return;
                        case "int" : case "java.lang.Integer" :
                            field.set(obj, unsigned ? (int)Long.parseLong(val, radix) : Integer.parseInt(val, radix));
                        return;
                        case "long" : case "java.lang.Long" : 
                            field.set(obj, unsigned ? Long.parseUnsignedLong(val, radix) : Long.parseLong(val, radix));
                        return;
                        case "float" : case "java.lang.Float" : 
                            field.set(obj, Float.parseFloat(val));
                        return;
                        case "double" : case "java.lang.Double" : 
                            field.set(obj, Double.parseDouble(val));
                        return;
                        default : 
                            throw new IllegalArgumentException("type ["+type+"] does not support");
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("["+val+"] is not ["+type+"] in "+clazz.getName()+"."+field.getName());
                }
            }
        });
    }
    
    /**
     * to class orders
     */
    @FunctionalInterface
    private static interface FixedDataBytesToClass {
        void order(Object obj, byte[] bytes, int offset) throws Exception;
    }
    
    /**
     * to bytes orders
     */
    @FunctionalInterface
    private static interface FixedDataClassToBytes {
        void order(Object obj, byte[] bytes, int offset) throws Exception;
    }
}
