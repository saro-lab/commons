package me.saro.commons.bytes.fd;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.saro.commons.Converter;
import me.saro.commons.bytes.Bytes;
import me.saro.commons.bytes.fd.annotations.BinaryData;
import me.saro.commons.bytes.fd.annotations.FixedDataClass;

/**
 * FixedMethodBinaryType
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public class FixedMethodBinaryType implements FixedMethod {
    
    final BinaryData meta;
    final String parentClassName;

    FixedMethodBinaryType(String parentClassName, BinaryData binaryData) {
        this.meta = binaryData;
        this.parentClassName = parentClassName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FixedMethodConsumer toBytes(Type genericReturnType, Method method) {
        
        int arrayLength = meta.arrayLength();
        int offset = meta.offset();
        String genericReturnTypeName = genericReturnType.getTypeName();
        Class<?> genericReturnTypeClass = genericReturnType.getClass();
        
        switch (genericReturnTypeName) {
            // object
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
            
            // basic array and list
            case "byte[]" : 
                return (bytes, idx, val) -> System.arraycopy(method.invoke(val), 0, bytes, offset + idx, arrayLength);
            case "java.lang.Byte[]" : 
                return (bytes, idx, val) -> System.arraycopy(Converter.toPrimitive((Byte[])method.invoke(val)), 0, bytes, offset + idx, arrayLength);
            
            case "short[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((short[])method.invoke(val)), 0, bytes, offset + idx, arrayLength * 2);
            case "java.lang.Short[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Short[])method.invoke(val))), 0, bytes, offset + idx, arrayLength * 2);
            case "java.util.List<java.lang.Short>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toShortArray((List<Short>)method.invoke(val))), 0, bytes, offset + idx, arrayLength * 2);
            
            case "int[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((int[])method.invoke(val)), 0, bytes, offset + idx, arrayLength * 4);
            case "java.lang.Integer[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Integer[])method.invoke(val))), 0, bytes, offset + idx, arrayLength * 4);
            case "java.util.List<java.lang.Integer>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toIntArray((List<Integer>)method.invoke(val))), 0, bytes, offset + idx, arrayLength * 4);
            
            case "long[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((long[])method.invoke(val)), 0, bytes, offset + idx, arrayLength * 8);
            case "java.lang.Long[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Long[])method.invoke(val))), 0, bytes, offset + idx, arrayLength * 8);
            case "java.util.List<java.lang.Long>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toLongArray((List<Long>)method.invoke(val))), 0, bytes, offset + idx, arrayLength * 8);
            
            case "float[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((float[])method.invoke(val)), 0, bytes, offset + idx, arrayLength * 4);
            case "java.lang.Float[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Float[])method.invoke(val))), 0, bytes, offset + idx, arrayLength * 4);
            case "java.util.List<java.lang.Float>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toFloatArray((List<Float>)method.invoke(val))), 0, bytes, offset + idx, arrayLength * 4);
            
            case "double[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes((double[])method.invoke(val)), 0, bytes, offset + idx, arrayLength * 8);
            case "java.lang.Double[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toPrimitive((Double[])method.invoke(val))), 0, bytes, offset + idx, arrayLength * 8);
            case "java.util.List<java.lang.Double>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.toBytes(Converter.toDoubleArray((List<Double>)method.invoke(val))), 0, bytes, offset + idx, arrayLength * 8);
            
            // there is not contained a basic types 
            default : 
                
                
                if (genericReturnTypeName.endsWith("[]")) {
                    
                    // -- @FixedDataClass[]
                    Class<?> componentType = genericReturnTypeClass.getComponentType();
                    if (componentType.getDeclaredAnnotation(FixedDataClass.class) != null) {
                        FixedData fd = FixedData.getInstance(componentType);
                        int size = fd.size();
                        return (bytes, idx, val) -> {
                            for (int i = 0 ; i < arrayLength ; i++) {
                                fd.bindBytes(Array.get(method.invoke(val), i), bytes, offset + idx + (size * i));
                            }
                        };
                    }
                    
                } else if (genericReturnTypeName.startsWith("java.util.List<")) {
                    
                    // -- List<@FixedDataClass>
                    try {
                        Class<?> clazz = Class.forName(genericReturnTypeName.substring(genericReturnTypeName.indexOf('<'), genericReturnTypeName.lastIndexOf('>'))).getClass();
                        if (clazz.getDeclaredAnnotation(FixedDataClass.class) != null) {
                            FixedData fd = FixedData.getInstance(clazz);
                            int size = fd.size();
                            return (bytes, idx, val) -> {
                                List<?> list = List.class.cast(method.invoke(val));
                                for (int i = 0 ; i < list.size() ; i++) {
                                    fd.bindBytes(list.get(i), bytes, offset + idx + (size * i));
                                }
                            };
                        }
                    } catch (ClassNotFoundException e) {}
                    
                } else if (genericReturnTypeClass.getDeclaredAnnotation(FixedDataClass.class) != null) {
                    
                    // -- @FixedDataClass
                    FixedData fd = FixedData.getInstance(genericReturnTypeClass);
                    return (bytes, idx, val) -> fd.bindBytes(method.invoke(val), bytes, offset + idx);
                    
                }
                
                System.out.println(genericReturnTypeClass);
                System.out.println(genericReturnTypeClass.getDeclaredAnnotation(FixedDataClass.class));
        }
        throw new IllegalArgumentException("does not support return type of "+genericReturnTypeName+" " + method.getName() + "() in " + parentClassName);
    }

    @Override
    public FixedMethodConsumer toClass(Type genericParameterType, Method method) {
        
        int arrayLength = meta.arrayLength();
        int offset = meta.offset();
        String genericParameterTypeName = genericParameterType.getTypeName();
        Class<?> genericParameterTypeClass = genericParameterType.getClass();
        
        switch (genericParameterTypeName) {
            
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
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Arrays.copyOfRange(bytes, idx + offset, idx + offset + arrayLength)));
            
            case "short[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShortArray(bytes, idx + offset, arrayLength));
            case "java.lang.Short[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toShortArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Short>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShortList(bytes, idx + offset, arrayLength));
            
            case "int[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toIntArray(bytes, idx + offset, arrayLength));
            case "java.lang.Integer[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toIntArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Integer>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toIntegerList(bytes, idx + offset, arrayLength));
                
            case "long[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLongArray(bytes, idx + offset, arrayLength));
            case "java.lang.Long[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toLongArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Long>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLongList(bytes, idx + offset, arrayLength));
            
            case "float[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloatArray(bytes, idx + offset, arrayLength));
            case "java.lang.Float[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toFloatArray(bytes, idx + offset, arrayLength))); 
            case "java.util.List<java.lang.Float>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloatList(bytes, idx + offset, arrayLength));
            
            case "double[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDoubleArray(bytes, idx + offset, arrayLength));
            case "java.lang.Double[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toDoubleArray(bytes, idx + offset, arrayLength)));
            case "java.util.List<java.lang.Double>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDoubleList(bytes, idx + offset, arrayLength));
            
            // there is not contained a basic types 
            default :
                
                if (genericParameterTypeName.endsWith("[]")) {
                    
                    // -- @FixedDataClass[]
                    Class<?> componentType = genericParameterTypeClass.getComponentType();
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
                    
                } else if (genericParameterTypeName.startsWith("java.util.List<")) {
                    
                    // -- List<@FixedDataClass>
                    try {
                        Class<?> clazz = Class.forName(genericParameterTypeName.substring(genericParameterTypeName.indexOf('<'), genericParameterTypeName.lastIndexOf('>'))).getClass();
                        if (clazz.getDeclaredAnnotation(FixedDataClass.class) != null) {
                            FixedData fd = FixedData.getInstance(clazz);
                            int size = fd.size();
                            return (bytes, idx, val) -> {
                                @SuppressWarnings("unchecked")
                                List<?> list = new ArrayList<>(List.class.cast(val));
                                for (int i = 0 ; i < list.size() ; i++) {
                                    list.add(fd.toClass(bytes, idx + offset + (size * i)));
                                }
                                method.invoke(val, list);
                            };
                        }
                    } catch (ClassNotFoundException e) {}
                    
                } else if (genericParameterTypeClass.getDeclaredAnnotation(FixedDataClass.class) != null) {
                    
                    // -- @FixedDataClass
                    FixedData fd = FixedData.getInstance(genericParameterTypeClass);
                    return (bytes, idx, val) -> method.invoke(val, fd.toClass(bytes, idx + offset));
                    
                }
        }
        throw new IllegalArgumentException("does not support parameter type of void " + method.getName() + "("+genericParameterTypeName+") in the " + parentClassName);
    }

}
