package me.saro.commons.bytes;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import me.saro.commons.bytes.annotations.FixedBinary;
import me.saro.commons.bytes.annotations.FixedText;
import me.saro.commons.bytes.annotations.FixedTextAlign;
import me.saro.commons.bytes.annotations.SplitTokenData;
import me.saro.commons.bytes.annotations.SplitTokenIndex;
import me.saro.commons.function.ThrowableConsumer;

/**
 * Split Token 
 * @author      PARK Yong Seo
 * @since       1.4
 */
public class SplitDataFormat<T> extends AbstractDataFormat implements DataFormat<T> {

    final Class<T> clazz;
    final SplitTokenData splitTokenData;
    final Supplier<T> newInstance;
    String charset;
    List<FixedDataBytesToClass> toClassOrders;
    List<FixedDataClassToBytes> toBytesOrders;
    
    SplitDataFormat(Class<T> clazz, Supplier<T> newInstance) {
        this.clazz = clazz;
        splitTokenData = clazz.getDeclaredAnnotation(SplitTokenData.class);
        this.newInstance = newInstance;
        init();
    }
    
    @Override
    public T toClass(byte[] bytes, int offset) {
        // TODO Auto-generated method stub
        return null;
    }
    
    toclass

    @Override
    public T toClassWithCheckSize(byte[] bytes) {
        return toClassWithCheckSize(new String(bytes, charset));
    }

    @Override
    public T toClassWithCheckSize(String line) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void bindBytes(byte[] outputBytes, int offset, T obj) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void bindBytes(OutputStream out, T obj) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public byte[] toBytes(T obj) {
        // TODO Auto-generated method stub
        return null;
    }
    
    private void init() {
        if (splitTokenData == null) {
            throw new IllegalArgumentException(clazz.getName() + " need to Declared @SplitTokenData Annotation");
        }
        if (splitTokenData.fieldCount() < 1) {
            throw new IllegalArgumentException(clazz.getName() + " have to Declared field count");
        }
        
        charset = splitTokenData.charset();
        toClassOrders = Collections.synchronizedList(new ArrayList<>(splitTokenData.fieldCount()));
        toBytesOrders = Collections.synchronizedList(new ArrayList<>(splitTokenData.fieldCount()));
        //FixedDataClassToBytes[] toBytesOrders;
        
        
        boolean infGetter = splitTokenData.ignoreNotFoundGetter();
        boolean infSetter = splitTokenData.ignoreNotFoundSetter();
        
        Stream.of(clazz.getDeclaredFields()).parallel().forEach(ThrowableConsumer.runtime(field -> {
            SplitTokenIndex index = field.getDeclaredAnnotation(SplitTokenIndex.class);
            setter(clazz, field.getName(), infSetter).ifPresent(e -> bindToClassOrder(e, index));
            getter(clazz, field.getName(), infGetter).ifPresent(e -> bindToBytesOrder(e, index));
        }));
    }
    
    /**
     * to class by binary data
     * @param field
     * @param da
     */
    private void bindToClassOrder(Method method, SplitTokenIndex order) {
        int dfOffset = da.offset();
        int arrayLength = da.arrayLength();
        String type = method.getParameterTypes()[0].getName();
        
        if (type.startsWith("[") && arrayLength < 1) {
            throw new IllegalArgumentException("arrayLength must over then 1");
        }
        
        toClassOrders.add((obj, bytes, offset) -> {
            int s = offset + dfOffset;
            
            switch (type) {
                case "[B" : case "[Ljava.lang.Byte;" :
                    method.invoke(obj, Arrays.copyOfRange(bytes, s, s + arrayLength));
                return;
                case "byte" : case "java.lang.Byte" :
                    method.invoke(obj, bytes[s]);
                return;
                case "short" : case "java.lang.Short" :
                    method.invoke(obj, Bytes.toShort(bytes, s));
                return;
                case "int" : case "java.lang.Integer" :
                    method.invoke(obj, Bytes.toInt(bytes, s));
                return;
                case "long" : case "java.lang.Long" : 
                    method.invoke(obj, Bytes.toLong(bytes, s));
                return;
                case "float" : case "java.lang.Float" : 
                    method.invoke(obj, Bytes.toFloat(bytes, s));
                return;
                case "double" : case "java.lang.Double" : 
                    method.invoke(obj, Bytes.toDouble(bytes, s));
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
    private void bindToClassOrder(Method method, SplitTokenIndex order) {
        
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
    private static interface SplitDataToClass {
        void order(String obj, int index) throws Exception;
    }
    
    /**
     * to bytes orders
     */
    @FunctionalInterface
    private static interface SplitDataToBytes {
        void order(Object obj, byte[] bytes, int offset) throws Exception;
    }
}
