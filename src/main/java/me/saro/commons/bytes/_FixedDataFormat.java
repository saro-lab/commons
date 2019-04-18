package me.saro.commons.bytes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import me.saro.commons.Converter;
import me.saro.commons.bytes.annotations.FixedBinary;
import me.saro.commons.bytes.annotations.FixedData;
import me.saro.commons.bytes.annotations.FixedText;
import me.saro.commons.bytes.annotations.FixedTextAlign;
import me.saro.commons.function.ThrowableConsumer;
import me.saro.commons.function.ThrowableSupplier;

/**
 * DataFormat 
 * @author      PARK Yong Seo
 * @since       1.0
 */
public class _FixedDataFormat<T> extends _AbstractDataFormat {
    
    final static Map<String, _FixedDataFormat<?>> STORE = new HashMap<>();
    
    final Class<T> clazz;
    final FixedData fixedData;
    final Supplier<T> newInstance;
    final List<FixedDataBytesToClass> toClassOrders = Collections.synchronizedList(new ArrayList<>());
    final List<FixedDataClassToBytes> toBytesOrders = Collections.synchronizedList(new ArrayList<>());
    
    @SuppressWarnings("unchecked")
    _FixedDataFormat(Class<T> clazz, Supplier<T> newInstance) {
        this.clazz = clazz;
        
        fixedData = clazz.getDeclaredAnnotation(FixedData.class);
        if (newInstance == null) {
            newInstance = ThrowableSupplier.runtime(() -> 
                (T)Arrays.asList(clazz.getDeclaredConstructors()).stream()
                    .filter(e -> e.getParameterCount() == 0).findFirst()
                    .orElseThrow(() -> new RuntimeException("need to new " + clazz.getName() + "() : default constructor"))
                    .newInstance());
        }
        this.newInstance = newInstance;
        init();
    }
    
    /**
     * create DataFormat<br>
     * this method is not cached<br>
     * recommend using getInstance 
     * @param clazz
     * @param newInstance
     * @return
     */
    public static <T> _FixedDataFormat<T> create(Class<T> clazz, Supplier<T> newInstance) {
        if (newInstance == null) {
            throw new IllegalArgumentException("must need to newInstance, do you want to just default constructor using FixedDataFormat.getInstance(...)");
        }
        return new _FixedDataFormat<>(clazz, newInstance);
    }
    
    /**
     * create DataFormat<br>
     * user defualt constructor
     * @deprecated using getInstance 
     * @param clazz
     * @return
     */
    @Deprecated
    public static <T> _FixedDataFormat<T> create(Class<T> clazz) {
        throw new RuntimeException("Deprecated this method, use FixedDataFormat.getInstance(...)");
    }
    
    /**
     * getInstance in cache store
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> _FixedDataFormat<T> getInstance(Class<T> clazz) {
        _FixedDataFormat<?> format;
        synchronized (STORE) {
            format = STORE.get(clazz.getName());
            if (format == null) {
                STORE.put(clazz.getName(), format = new _FixedDataFormat<>(clazz, null));
            }
        }
        return (_FixedDataFormat<T>) format;
    }
    
    /**
     * to class
     * @param bytes
     * @param offset
     * @return
     */
    public void bindClass(T clazz, byte[] bytes, int offset) {
        toClassOrders.parallelStream().forEach(ThrowableConsumer.runtime(e -> e.order(clazz, bytes, offset)));
    }
    
    /**
     * to class
     * @param bytes
     * @return
     */
    public void bindClass(T clazz, byte[] bytes) {
        bindClass(clazz, bytes, 0);
    }
    
    /**
     * to class
     * @param bytes
     * @param offset
     * @return
     */
    public T toClass(byte[] bytes, int offset) {
        T t = newInstance.get();
        toClassOrders.parallelStream().forEach(ThrowableConsumer.runtime(e -> e.order(t, bytes, offset)));
        return t;
    }
    
    /**
     * to class
     * @param bytes
     * @return
     */
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
    
    public T toClassWithCheckSize(String line, String charset) {
        try {
            return toClassWithCheckSize(line.getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * to bytes and bind byte[]
     * @param outputBytes
     * @param offset
     * @param obj
     */
    public void bindBytes(byte[] outputBytes, int offset, T obj) {
        Arrays.fill(outputBytes, offset, offset + fixedData.size(), fixedData.fill());
        toBytesOrders.parallelStream().forEach(ThrowableConsumer.runtime(e -> e.order(obj, outputBytes, offset)));
    }
    
    /**
     * to bytes and bind OutputStream
     * @param out
     * @param obj
     * @throws IOException
     */
    public void bindBytes(OutputStream out, T obj) throws IOException {
        byte[] buf = new byte[fixedData.size()];
        bindBytes(buf, 0, obj);
        out.write(buf);
    }
    
    /**
     * to bytes
     * @param obj
     * @return
     */
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
     * fixed data size
     * @return
     */
    public int getFixedDataSize() {
        return fixedData.size();
    }
    
    /**
     * init fixed data format
     * @return
     */
    private void init() {
        if (fixedData == null) {
            throw new IllegalArgumentException(clazz.getName() + " need to Declared @FixedData Annotation");
        }
        if (fixedData.size() < 1) {
            throw new IllegalArgumentException(clazz.getName() + " need to Declared size");
        }
        
        boolean infGetter = fixedData.ignoreNotFoundGetter();
        boolean infSetter = fixedData.ignoreNotFoundSetter();
        
        Stream.of(clazz.getDeclaredFields()).parallel().forEach(ThrowableConsumer.runtime(field -> {
            FixedBinary binary = field.getDeclaredAnnotation(FixedBinary.class);
            FixedText text = field.getDeclaredAnnotation(FixedText.class);
            if (binary != null) {
                setter(clazz, field.getName(), infSetter).ifPresent(e -> bindToClassOrder(e, binary));
                getter(clazz, field.getName(), infGetter).ifPresent(e -> bindToBytesOrder(e, binary));
            } else if (text != null) {
                setter(clazz, field.getName(), infSetter).ifPresent(e -> bindToClassOrder(e, text));
                getter(clazz, field.getName(), infGetter).ifPresent(e -> bindToBytesOrder(e, text));
            }
        }));
    }
    
    /**
     * to bytes by field value
     * @param field
     * @param da
     */
    @SuppressWarnings("unchecked")
    private void bindToBytesOrder(Method method, FixedBinary da) {
        int dfOffset = da.offset();
        int arrayLength = da.arrayLength();
        Type typeClass = method.getGenericReturnType();
        String type = typeClass.getTypeName();
        
        toBytesOrders.add((obj, bytes, offset) -> {
            
            int s = offset + dfOffset;
            
            if (obj == null) {
                return;
            }
            Object val = method.invoke(obj);
            
            if (val == null) {
                return;
            }
            
            switch (type) {
                // object
                case "byte" : case "java.lang.Byte" : bytes[s] = (byte)val; return;
                case "short" : case "java.lang.Short" : System.arraycopy(Bytes.toBytes((short)val), 0, bytes, s, 2); return;
                case "int" : case "java.lang.Integer" : System.arraycopy(Bytes.toBytes((int)val), 0, bytes, s, 4); return;
                case "long" : case "java.lang.Long" : System.arraycopy(Bytes.toBytes((long)val), 0, bytes, s, 8); return;
                case "float" : case "java.lang.Float" : System.arraycopy(Bytes.toBytes((float)val), 0, bytes, s, 4); return;
                case "double" : case "java.lang.Double" : System.arraycopy(Bytes.toBytes((double)val), 0, bytes, s, 8); return;
                
                // array
                case "byte[]" : System.arraycopy(val, 0, bytes, s, arrayLength); return;
                case "java.lang.Byte[]" : System.arraycopy(Converter.toPrimitive((Byte[])val), 0, bytes, s, arrayLength); return;
                
                case "short[]" : System.arraycopy(Bytes.toBytes((short[])val), 0, bytes, s, arrayLength * 2); return;
                case "java.lang.Short[]" : System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Short[])val)), 0, bytes, s, arrayLength * 2); return;
                case "java.util.List<java.lang.Short>" : System.arraycopy(Bytes.toBytes(Converter.toShortArray((List<Short>)val)), 0, bytes, s, arrayLength * 2); return;
                
                case "int[]" : System.arraycopy(Bytes.toBytes((int[])val), 0, bytes, s, arrayLength * 4); return;
                case "java.lang.Integer[]" : System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Integer[])val)), 0, bytes, s, arrayLength * 4); return;
                case "java.util.List<java.lang.Integer>" : System.arraycopy(Bytes.toBytes(Converter.toIntArray((List<Integer>)val)), 0, bytes, s, arrayLength * 4); return;
                
                case "long[]" : System.arraycopy(Bytes.toBytes((long[])val), 0, bytes, s, arrayLength * 8); return;
                case "java.lang.Long[]" : System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Long[])val)), 0, bytes, s, arrayLength * 8); return;
                case "java.util.List<java.lang.Long>" : System.arraycopy(Bytes.toBytes(Converter.toLongArray((List<Long>)val)), 0, bytes, s, arrayLength * 8); return;
                
                case "float[]" : System.arraycopy(Bytes.toBytes((float[])val), 0, bytes, s, arrayLength * 4); return;
                case "java.lang.Float[]" : System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Float[])val)), 0, bytes, s, arrayLength * 4); return;
                case "java.util.List<java.lang.Float>" : System.arraycopy(Bytes.toBytes(Converter.toFloatArray((List<Float>)val)), 0, bytes, s, arrayLength * 4); return;
                
                case "double[]" : System.arraycopy(Bytes.toBytes((double[])val), 0, bytes, s, arrayLength * 8); return;
                case "java.lang.Double[]" : System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Double[])val)), 0, bytes, s, arrayLength * 8); return;
                case "java.util.List<java.lang.Double>" : System.arraycopy(Bytes.toBytes(Converter.toDoubleArray((List<Double>)val)), 0, bytes, s, arrayLength * 8); return;
                
                default : 
                    
                    byte[] buf;
                    
                    if (type.endsWith("[]")) { // array
                        @SuppressWarnings("rawtypes") final Class clazz = val.getClass().getComponentType();
                        if (clazz.getDeclaredAnnotation(FixedData.class) != null) {
                            int pos = s;
                            for (int i = 0 ; i < arrayLength ; i++) {
                                buf = _FixedDataFormat.getInstance(clazz).toBytes(Array.get(val, i));
                                System.arraycopy(buf, 0, bytes, pos, buf.length);
                                pos += buf.length;
                            }
                            return;
                        }
                    } else if (type.startsWith("java.util.List<")) { // list
                        // not support yet
                    } else { // object
                        @SuppressWarnings("rawtypes") final Class clazz = val.getClass();
                        buf = _FixedDataFormat.getInstance(clazz).toBytes(clazz.cast(val));
                        System.arraycopy(buf, 0, bytes, s, buf.length);
                        return;
                    }
                    
                    throw new IllegalArgumentException("does not support return type of "+type+" " + method.getName() + "()");
            }
        });
    }
    
    /**
     * to bytes by field value
     * @param field
     * @param da
     */
    private void bindToBytesOrder(Method method, FixedText da) {
        int dfOffset = da.offset();
        int dfLength = da.length();
        boolean isLeft = da.align() == FixedTextAlign.left;
        String type = method.getReturnType().getName();
        boolean unsigned = da.unsigned();
        String charset = "".equals(da.charset()) ? fixedData.charset() : da.charset();
        int radix = da.radix();
        
        toBytesOrders.add((clazz, bytes, offset) -> {
            int s = offset + dfOffset;
            byte fill = da.fill();

            if (clazz == null) {
                return;
            }
            
            Object val = method.invoke(clazz);
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
                throw new IllegalArgumentException("["+new String(vbytes, charset)+"] is out of range of "+method.getName()+"()");
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
    @SuppressWarnings("unchecked")
    private void bindToClassOrder(Method method, FixedBinary da) {
        int dfOffset = da.offset();
        int arrayLength = da.arrayLength();
        String type = method.getGenericParameterTypes()[0].getTypeName();
        
        if ((type.indexOf("[]") != -1 || type.indexOf("java.util.List<") != -1) && arrayLength < 1) {
            throw new IllegalArgumentException("arrayLength in @FixedBinary void " + method.getName() + "() must much more then 1");
        }
        
        toClassOrders.add((obj, bytes, offset) -> {
            int s = offset + dfOffset;
            
            switch (type) {
                // object
                case "byte" : case "java.lang.Byte" : method.invoke(obj, bytes[s]); return;
                case "short" : case "java.lang.Short" : method.invoke(obj, Bytes.toShort(bytes, s)); return;
                case "int" : case "java.lang.Integer" : method.invoke(obj, Bytes.toInt(bytes, s)); return;
                case "long" : case "java.lang.Long" : method.invoke(obj, Bytes.toLong(bytes, s)); return;
                case "float" : case "java.lang.Float" : method.invoke(obj, Bytes.toFloat(bytes, s)); return;
                case "double" : case "java.lang.Double" : method.invoke(obj, Bytes.toDouble(bytes, s)); return;
                
                // array
                case "byte[]" : method.invoke(obj, Arrays.copyOfRange(bytes, s, s + arrayLength)); return;
                case "java.lang.Byte[]" : method.invoke(obj, (Object)Converter.toUnPrimitive(Arrays.copyOfRange(bytes, s, s + arrayLength))); return;
                
                case "short[]" : method.invoke(obj, Bytes.toShortArray(bytes, s, arrayLength)); return;
                case "java.lang.Short[]" : method.invoke(obj, (Object)Converter.toUnPrimitive(Bytes.toShortArray(bytes, s, arrayLength))); return;
                case "java.util.List<java.lang.Short>" : method.invoke(obj, Bytes.toShortList(bytes, s, arrayLength)); return;
                
                case "int[]" : method.invoke(obj, Bytes.toIntArray(bytes, s, arrayLength)); return;
                case "java.lang.Integer[]" : method.invoke(obj, (Object)Converter.toUnPrimitive(Bytes.toIntArray(bytes, s, arrayLength))); return;
                case "java.util.List<java.lang.Integer>" : method.invoke(obj, Bytes.toIntegerList(bytes, s, arrayLength)); return;
                    
                case "long[]" : method.invoke(obj, Bytes.toLongArray(bytes, s, arrayLength)); return;
                case "java.lang.Long[]" : method.invoke(obj, (Object)Converter.toUnPrimitive(Bytes.toLongArray(bytes, s, arrayLength))); return;
                case "java.util.List<java.lang.Long>" : method.invoke(obj, Bytes.toLongList(bytes, s, arrayLength)); return;
                
                case "float[]" : method.invoke(obj, Bytes.toFloatArray(bytes, s, arrayLength)); return;
                case "java.lang.Float[]" : method.invoke(obj, (Object)Converter.toUnPrimitive(Bytes.toFloatArray(bytes, s, arrayLength))); return; 
                case "java.util.List<java.lang.Float>" : method.invoke(obj, Bytes.toFloatList(bytes, s, arrayLength)); return;
                
                case "double[]" : method.invoke(obj, Bytes.toDoubleArray(bytes, s, arrayLength)); return;
                case "java.lang.Double[]" : method.invoke(obj, (Object)Converter.toUnPrimitive(Bytes.toDoubleArray(bytes, s, arrayLength))); return;
                case "java.util.List<java.lang.Double>" : method.invoke(obj, Bytes.toDoubleList(bytes, s, arrayLength)); return;
                
                default : 
                    
                    if (type.endsWith("[]")) { // array
                        @SuppressWarnings("rawtypes")
                        Class clazz = method.getParameterTypes()[0].getComponentType();
                        if (clazz.getDeclaredAnnotation(FixedData.class) != null) {
                            Object arr = Array.newInstance(clazz, arrayLength);
                            @SuppressWarnings({ "rawtypes" })
                            _FixedDataFormat fdf = _FixedDataFormat.getInstance(clazz);
                            for (int i = 0 ; i < arrayLength ; i++) {
                                Array.set(arr, i, fdf.toClass(bytes, s + (fdf.getFixedDataSize() * i)));
                            }
                            method.invoke(obj, arr);
                        }
                        return;
                    } else if (type.startsWith("java.util.List<")) {
                        // not support yet
                    } else {
                        method.invoke(obj, _FixedDataFormat.getInstance(method.getParameterTypes()[0]).toClass(bytes, s));
                        return;
                    }
                    
                    throw new IllegalArgumentException("does not support parameter type of void " + method.getName() + "("+type+")");
            }
        });
    }
    
    /**
     * to class by text data
     * @param field
     * @param da
     */
    private void bindToClassOrder(Method method, FixedText da) {
        
        // it is not setter method
        if (method.getParameterCount() != 1) {
            return;
        }
        
        int dfOffset = da.offset();
        int dfLength = da.length();
        byte dfFill = da.fill();
        boolean isLeft = da.align() == FixedTextAlign.left;
        String type = method.getParameterTypes()[0].getName();
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
                method.invoke(obj, val);
            } else {
                try {
                    switch (type) {
                        
                    
                        case "byte" : case "java.lang.Byte" :
                            method.invoke(obj, (byte)Integer.parseInt(val, radix));
                        return;
                        case "short" : case "java.lang.Short" :
                            method.invoke(obj, (short)Integer.parseInt(val, radix));
                        return;
                        case "int" : case "java.lang.Integer" :
                            method.invoke(obj, unsigned ? (int)Long.parseLong(val, radix) : Integer.parseInt(val, radix));
                        return;
                        case "long" : case "java.lang.Long" : 
                            method.invoke(obj, unsigned ? Long.parseUnsignedLong(val, radix) : Long.parseLong(val, radix));
                        return;
                        case "float" : case "java.lang.Float" : 
                            method.invoke(obj, Float.parseFloat(val));
                        return;
                        case "double" : case "java.lang.Double" : 
                            method.invoke(obj, Double.parseDouble(val));
                        return;
                        default : 
                            throw new IllegalArgumentException("type ["+type+"] does not support");
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("["+val+"] is not ["+type+"] in "+clazz.getName()+"."+method.getName()+"()");
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
