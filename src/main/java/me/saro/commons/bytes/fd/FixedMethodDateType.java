package me.saro.commons.bytes.fd;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import me.saro.commons.bytes.Bytes;
import me.saro.commons.bytes.fd.annotations.DateData;
import me.saro.commons.bytes.fd.annotations.TextData;

/**
 * FixedMethodDateType
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public class FixedMethodDateType implements FixedMethod {
    
    final DateData meta;
    final String parentClassName;

    FixedMethodDateType(String parentClassName, DateData dateDate) {
        this.meta = dateDate;
        this.parentClassName = parentClassName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FixedMethodConsumer toBytes(Method method) {
        
        int offset = meta.offset();
        int length = meta.length();
        Type genericReturnType = method.getGenericReturnType();
        String genericReturnTypeName = genericReturnType.getTypeName();
        Class<?> returnTypeClass = method.getReturnType();
        
        switch (genericReturnTypeName) {
            case "java.lang.String" :
            
            case "byte" : case "java.lang.Byte" :
                return (bytes, idx, val) -> bytes[offset + idx] = (byte)method.invoke(val);
                
            case "short" : case "java.lang.Short" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((short)method.invoke(val)), 0, bytes, offset + idx, 2);
                
            case "int" : case "java.lang.Integer" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((int)method.invoke(val)), 0, bytes, offset + idx, 4);
                
            case "long" : case "java.lang.Long" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((long)method.invoke(val)), 0, bytes, offset + idx, 8);
                
            case "float" : case "java.lang.Float" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((float)method.invoke(val)), 0, bytes, offset + idx, 4);
                
            case "double" : case "java.lang.Double" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((double)method.invoke(val)), 0, bytes, offset + idx, 8);
            
            
        }
        throw new IllegalArgumentException("does not support return type of "+genericReturnTypeName+" " + method.getName() + "() in " + parentClassName);
    }

    @Override
    public FixedMethodConsumer toClass(Method method) {
        
        int offset = meta.offset();
        int length = meta.length();
        Type genericParameterType = method.getGenericParameterTypes()[0];
        String genericParameterTypeName = genericParameterType.getTypeName();
        Class<?> parameterTypeClass = method.getParameterTypes()[0];
        
        switch (genericParameterTypeName) {
            case "java.lang.String" :
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
            
        }
        throw new IllegalArgumentException("does not support parameter type of void " + method.getName() + "("+genericParameterTypeName+") in the " + parentClassName);
    }

}
