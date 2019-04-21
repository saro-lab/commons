package me.saro.commons.bytes.fd;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import me.saro.commons.Converter;
import me.saro.commons.bytes.Bytes;
import me.saro.commons.bytes._FixedDataFormat;
import me.saro.commons.bytes.fd.annotations.BinaryData;
import me.saro.commons.bytes.fd.annotations.FixedDataClass;
import me.saro.commons.bytes.fd.annotations.TextData;
import me.saro.commons.function.ThrowableTriConsumer;

/**
 * FixedMethodBinaryType
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public class FixedMethodBinaryType implements FixedMethod {
    
    final BinaryData meta;

    FixedMethodBinaryType(BinaryData binaryData) {
        this.meta = binaryData;
    }

    @Override
    public Consumer toByte(Class parameterType, String genericParameterType) {
        
        
    }

    @Override
    public FixedMethodToClassConsumer toClass(Class returnType, String genericReturnType, Method method) throws InvocationTargetException {
        
        final int arrayLength = meta.arrayLength();
        final int offset = meta.offset();
        
        switch (genericReturnType) {
            
            // object
            case "byte" : case "java.lang.Byte" : 
                return (bytes, idx, val) -> method.invoke(val, bytes[idx + offset]);
                
            case "short" : case "java.lang.Short" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShort(bytes, idx + offset));
                
            case "int" : case "java.lang.Integer" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toInt(bytes, idx + offset));
                
            case "long" : case "java.lang.Long" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLong(bytes, idx + offset));
                
            case "float" : case "java.lang.Float" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloat(bytes, idx + offset));
                
            case "double" : case "java.lang.Double" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDouble(bytes, idx + offset));
            
            // basic array and list
            case "byte[]" : 
                return (bytes, idx, val) -> method.invoke(val, Arrays.copyOfRange(bytes, idx + offset, idx + offset + arrayLength));
            case "java.lang.Byte[]" : 
                return (bytes, idx, val) -> method.invoke(val, Converter.toUnPrimitive(Arrays.copyOfRange(bytes, idx + offset, idx + offset + arrayLength)));
            
            case "short[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShortArray(bytes, idx + offset, arrayLength));
            case "java.lang.Short[]" : 
                return (bytes, idx, val) -> method.invoke(val, Converter.toUnPrimitive(Bytes.toShortArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Short>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShortList(bytes, idx + offset, arrayLength));
            
            case "int[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toIntArray(bytes, idx + offset, arrayLength));
            case "java.lang.Integer[]" : 
                return (bytes, idx, val) -> method.invoke(val, Converter.toUnPrimitive(Bytes.toIntArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Integer>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toIntegerList(bytes, idx + offset, arrayLength));
                
            case "long[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLongArray(bytes, idx + offset, arrayLength));
            case "java.lang.Long[]" : 
                return (bytes, idx, val) -> method.invoke(val, Converter.toUnPrimitive(Bytes.toLongArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Long>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLongList(bytes, idx + offset, arrayLength));
            
            case "float[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloatArray(bytes, idx + offset, arrayLength));
            case "java.lang.Float[]" : 
                return (bytes, idx, val) -> method.invoke(val, Converter.toUnPrimitive(Bytes.toFloatArray(bytes, idx + offset, arrayLength))); 
            case "java.util.List<java.lang.Float>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloatList(bytes, idx + offset, arrayLength));
            
            case "double[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDoubleArray(bytes, idx + offset, arrayLength));
            case "java.lang.Double[]" : 
                return (bytes, idx, val) -> method.invoke(val, Converter.toUnPrimitive(Bytes.toDoubleArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Double>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDoubleList(bytes, idx + offset, arrayLength));
            
            default : // there is not contained a basic types 
                
                if (genericReturnType.endsWith("[]")) {
                    // -- @FixedDataClass[]
                    Class componentType = returnType.getComponentType();
                    // type check : support only the FixedDataClass
                    if (componentType.getDeclaredAnnotation(FixedDataClass.class) != null) {
                        FixedData fd = FixedData.getInstance(componentType);
                        int size = fd.size();
                        return (bytes, idx, val) -> {
                            Object arr = Array.newInstance(componentType, arrayLength);
                            for (int i = 0 ; i < arrayLength ; i++) {
                                Array.set(arr, i, fd.toClass(bytes, idx + offset + (size * i)));
                            }
                            method.invoke(val, arr);
                        };
                    }
                } else if (genericReturnType.startsWith("java.util.List<")) {
                    // -- List<@FixedDataClass>
                    // not support yet
                } else if (returnType.getDeclaredAnnotation(FixedDataClass.class) != null) {
                    // -- @FixedDataClass
                    FixedData fd = FixedData.getInstance(returnType);
                    return (bytes, idx, val) -> method.invoke(val, fd.toClass(bytes, idx + offset));
                }
        }
        throw new IllegalArgumentException("does not support parameter type of void " + method.getName() + "("+genericReturnType+")");
    }

}
