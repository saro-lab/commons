package me.saro.commons.bytes.fd;

import java.lang.reflect.Method;
import java.util.Arrays;

import lombok.SneakyThrows;
import me.saro.commons.bytes.Bytes;
import me.saro.commons.bytes.fd.annotations.FixedDataClass;
import me.saro.commons.bytes.fd.annotations.TextData;
import me.saro.commons.bytes.fd.annotations.TextDataAlign;

/**
 * FixedMethodTextType
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public class FixedMethodTextType implements FixedMethod {
    
    final static byte[] EMPTY_BYTES = new byte[0];
    
    final TextData meta;
    final String parentClassName;
    final FixedDataClass fixedDataClassInfo;

    FixedMethodTextType(FixedDataClass fixedDataClassInfo, String parentClassName, TextData textData) {
        this.fixedDataClassInfo = fixedDataClassInfo;
        this.meta = textData;
        this.parentClassName = parentClassName;
    }

    @SneakyThrows
    @Override
    public FixedMethodConsumer toBytes(Method method) {
        
        int offset = meta.offset();
        int length = meta.length();
        boolean isLeft = meta.align() == TextDataAlign.left;
        boolean unsigned = meta.unsigned();
        String charset = "".equals(meta.charset()) ? fixedDataClassInfo.charset() : meta.charset();
        int radix = meta.radix();
        byte fill = meta.fill();
        String genericReturnTypeName = method.getGenericReturnType().getTypeName();
        
        switch (genericReturnTypeName) {
            case "java.lang.String" :
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, toBytes((String)method.invoke(val), charset), length, fill, isLeft, method);
                
            case "byte" : case "java.lang.Byte" :
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, Integer.toString(
                        unsigned 
                            ? Byte.toUnsignedInt((byte)method.invoke(val)) 
                            : (int)(byte)method.invoke(val), radix).getBytes()
                        , length, fill, isLeft, method);
                
            case "short" : case "java.lang.Short" : 
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, Integer.toString(
                        unsigned 
                            ? Short.toUnsignedInt((short)method.invoke(val)) 
                            : (int)(short)method.invoke(val), radix).getBytes()
                        , length, fill, isLeft, method);
                
            case "int" : case "java.lang.Integer" : 
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, (
                        unsigned 
                            ? Integer.toUnsignedString((int)method.invoke(val), radix) 
                            : Integer.toString((int)method.invoke(val), radix)).getBytes()
                        , length, fill, isLeft, method);
                
            case "long" : case "java.lang.Long" : 
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, (
                        unsigned 
                            ? Long.toUnsignedString((long)method.invoke(val), radix) 
                            : Long.toString((long)method.invoke(val), radix)).getBytes()
                        , length, fill, isLeft, method);
                
            case "float" : case "java.lang.Float" : 
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, (Float.toString((float)method.invoke(val))).getBytes(), length, fill, isLeft, method);
                
            case "double" : case "java.lang.Double" : 
                return (bytes, idx, val) -> bindBytes(bytes, offset + idx, (Double.toString((double)method.invoke(val))).getBytes(), length, fill, isLeft, method);
            
            
        }
        throw new IllegalArgumentException("does not support return type of "+genericReturnTypeName+" " + method.getName() + "() in " + parentClassName);
    }

    @Override
    public FixedMethodConsumer toClass(Method method) {
        
        int offset = meta.offset();
        int length = meta.length();
        boolean isLeft = meta.align() == TextDataAlign.left;
        boolean unsigned = meta.unsigned();
        String charset = "".equals(meta.charset()) ? fixedDataClassInfo.charset() : meta.charset();
        int radix = meta.radix();
        byte fill = meta.fill();
        boolean emptyIsNull = meta.emptyIsNull();
        String genericParameterTypeName = method.getGenericParameterTypes()[0].getTypeName();
        
        switch (genericParameterTypeName) {
            case "java.lang.String" :
                return (bytes, idx, val) -> method.invoke(val, newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, emptyIsNull));
                
            case "byte" : case "java.lang.Byte" : 
                return (bytes, idx, val) -> method.invoke(val, (byte)Integer.parseInt(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false), radix));
                
            case "short" : case "java.lang.Short" : 
                return (bytes, idx, val) -> method.invoke(val, (short)Integer.parseInt(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false), radix));
                
            case "int" : case "java.lang.Integer" : 
                return (bytes, idx, val) -> method.invoke(val, 
                        unsigned 
                            ? (int)Long.parseLong(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false), radix) 
                            : Integer.parseInt(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false), radix));
                
            case "long" : case "java.lang.Long" : 
                return (bytes, idx, val) -> method.invoke(val, 
                        unsigned 
                            ? Long.parseUnsignedLong(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false), radix) 
                            : Long.parseLong(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false), radix));
                
            case "float" : case "java.lang.Float" : 
                return (bytes, idx, val) -> method.invoke(val, Float.parseFloat(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false)));
                
            case "double" : case "java.lang.Double" : 
                return (bytes, idx, val) -> method.invoke(val, Double.parseDouble(newString(bytes, getIndexWithoutFill(bytes, offset + idx, length, fill, isLeft), charset, false)));
            
        }
        throw new IllegalArgumentException("does not support parameter type of void " + method.getName() + "("+genericParameterTypeName+") in the " + parentClassName);
    }
    
    @SneakyThrows
    public byte[] toBytes(String text, String charset) {
        return text != null ? text.getBytes(charset) : EMPTY_BYTES;
    }
    
    @SneakyThrows
    public String newString(byte[] bytes, int[] fi, String charset, boolean emptyIsNull) {
        return fi[0] == fi[1] ? (emptyIsNull ? null : "") : new String(bytes, fi[0], fi[1] - fi[0], charset);
    }
    
    private int[] getIndexWithoutFill(byte[] data, int dataOffset, int dataLength, byte fill, boolean isLeft) {
        
        if (isLeft) {
            // - left align (default)
            int sidx = dataOffset;
            int eidx = dataOffset;
            for (int i = (dataOffset + dataLength - 1) ; i >= dataOffset ; i--) {
                if (fill != data[i]) {
                    eidx = i + 1;
                    break;
                }
            }
            return new int[]{sidx, eidx};
        } else {
            // - right align
            int lidx = dataOffset + dataLength;
            int sidx = lidx;
            int eidx = lidx;
            for (int i = dataOffset ; i < lidx ; i++) {
                if (fill != data[i]) {
                    sidx = i;
                    break;
                }
            }
            return new int[]{sidx, eidx};
        }
    }
    
    private void bindBytes(byte[] out, int outOffset, byte[] data, int dataLength, byte fill, boolean isLeft, Method method) {
        int size = data.length;
        
        if (size > dataLength) {
            // - over
            throw new RuntimeException("overflow " + parentClassName + "." + method.getName() + "() : limit["+dataLength+"], size["+size+"] : hex_data " + Bytes.toHex(data));
        } else if (size == dataLength) {
            // - same
            System.arraycopy(data, 0, out, outOffset, dataLength);
        }
        
        // - small
        if (isLeft) {
            // - left align (default)
            System.arraycopy(data, 0, out, outOffset, size);
            Arrays.fill(out, outOffset + size, outOffset + dataLength, fill);
        } else {
            // - right align
            int offsetDef = outOffset + dataLength - size;
            System.arraycopy(data, 0, out, offsetDef, size);
            Arrays.fill(out, outOffset, offsetDef, fill);
        }
    }

}
