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
    final FixedDataClass fixedDataClassInfo;

    FixedMethodBinaryType(FixedDataClass fixedDataClassInfo, String parentClassName, BinaryData binaryData) {
        this.fixedDataClassInfo = fixedDataClassInfo;
        this.meta = binaryData;
        this.parentClassName = parentClassName;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FixedMethodConsumer toBytes(Method method) {
        
        int arrayLength = meta.arrayLength();
        int offset = meta.offset();
        boolean le = !fixedDataClassInfo.bigEndian();
        Type genericReturnType = method.getGenericReturnType();
        String genericReturnTypeName = genericReturnType.getTypeName();
        Class<?> returnTypeClass = method.getReturnType();
        
        switch (genericReturnTypeName) {
            // object
            case "byte" : case "java.lang.Byte" :
                return (bytes, idx, val) -> bytes[offset + idx] = (byte)method.invoke(val);
                
            case "short" : case "java.lang.Short" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((short)method.invoke(val)), le), 0, bytes, offset + idx, 2);
                
            case "int" : case "java.lang.Integer" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((int)method.invoke(val)), le), 0, bytes, offset + idx, 4);
                
            case "long" : case "java.lang.Long" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((long)method.invoke(val)), le), 0, bytes, offset + idx, 8);
                
            case "float" : case "java.lang.Float" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((float)method.invoke(val)), le), 0, bytes, offset + idx, 4);
                
            case "double" : case "java.lang.Double" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((double)method.invoke(val)), le), 0, bytes, offset + idx, 8);
            
            // basic array and list
            case "byte[]" : 
                return (bytes, idx, val) -> System.arraycopy(method.invoke(val), 0, bytes, offset + idx, arrayLength);
            case "java.lang.Byte[]" : 
                return (bytes, idx, val) -> System.arraycopy(Converter.toPrimitive((Byte[])method.invoke(val)), 0, bytes, offset + idx, arrayLength);
            
            case "short[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((short[])method.invoke(val)), le), 0, bytes, offset + idx, arrayLength * 2);
            case "java.lang.Short[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toPrimitive((Short[])method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 2);
            case "java.util.List<java.lang.Short>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toShortArray((List<Short>)method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 2);
            
            case "int[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((int[])method.invoke(val)), le), 0, bytes, offset + idx, arrayLength * 4);
            case "java.lang.Integer[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toPrimitive((Integer[])method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 4);
            case "java.util.List<java.lang.Integer>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toIntArray((List<Integer>)method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 4);
            
            case "long[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((long[])method.invoke(val)), le), 0, bytes, offset + idx, arrayLength * 8);
            case "java.lang.Long[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toPrimitive((Long[])method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 8);
            case "java.util.List<java.lang.Long>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toLongArray((List<Long>)method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 8);
            
            case "float[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((float[])method.invoke(val)), le), 0, bytes, offset + idx, arrayLength * 4);
            case "java.lang.Float[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toPrimitive((Float[])method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 4);
            case "java.util.List<java.lang.Float>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toFloatArray((List<Float>)method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 4);
            
            case "double[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes((double[])method.invoke(val)), le), 0, bytes, offset + idx, arrayLength * 8);
            case "java.lang.Double[]" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toPrimitive((Double[])method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 8);
            case "java.util.List<java.lang.Double>" : 
                return (bytes, idx, val) -> System.arraycopy(Bytes.copy(Bytes.toBytes(Converter.toDoubleArray((List<Double>)method.invoke(val))), le), 0, bytes, offset + idx, arrayLength * 8);
            
            // there is not contained a basic types 
            default : 
                
                
                if (genericReturnTypeName.endsWith("[]")) {
                    
                    // -- @FixedDataClass[]
                    Class<?> componentType = returnTypeClass.getComponentType();
                    if (componentType.getDeclaredAnnotation(FixedDataClass.class) != null) {
                        FixedData fd = FixedData.getInstance(componentType);
                        int size = fd.meta().size();
                        return (bytes, idx, val) -> {
                            for (int i = 0 ; i < arrayLength ; i++) {
                                fd.bindBytes(Array.get(method.invoke(val), i), bytes, offset + idx + (size * i));
                            }
                        };
                    }
                    
                } else if (genericReturnTypeName.startsWith("java.util.List<")) {
                    
                    // -- List<@FixedDataClass>
                    try {
                        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(genericReturnTypeName.substring(genericReturnTypeName.indexOf('<') + 1, genericReturnTypeName.lastIndexOf('>')));
                        if (clazz.getDeclaredAnnotation(FixedDataClass.class) != null) {
                            FixedData fd = FixedData.getInstance(clazz);
                            int size = fd.meta().size();
                            return (bytes, idx, val) -> {
                                List<?> list = List.class.cast(method.invoke(val));
                                for (int i = 0 ; i < list.size() ; i++) {
                                    fd.bindBytes(list.get(i), bytes, offset + idx + (size * i));
                                }
                            };
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    
                } else if (returnTypeClass.getDeclaredAnnotation(FixedDataClass.class) != null) {
                    
                    // -- @FixedDataClass
                    FixedData fd = FixedData.getInstance(returnTypeClass);
                    return (bytes, idx, val) -> fd.bindBytes(method.invoke(val), bytes, offset + idx);
                    
                }
        }
        throw new IllegalArgumentException("does not support return type of "+genericReturnTypeName+" " + method.getName() + "() in " + parentClassName);
    }

    @Override
    public FixedMethodConsumer toClass(Method method) {
        
        int arrayLength = meta.arrayLength();
        int offset = meta.offset();
        boolean le = !fixedDataClassInfo.bigEndian();
        Type genericParameterType = method.getGenericParameterTypes()[0];
        String genericParameterTypeName = genericParameterType.getTypeName();
        Class<?> parameterTypeClass = method.getParameterTypes()[0];
        
        switch (genericParameterTypeName) {
            
            // object
            case "byte" : case "java.lang.Byte" : 
                return (bytes, idx, val) -> method.invoke(val, bytes[idx + offset]);
                
            case "short" : case "java.lang.Short" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShort(Bytes.copy(bytes, idx + offset, 2, le)));
                
            case "int" : case "java.lang.Integer" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toInt(Bytes.copy(bytes, idx + offset, 4, le)));
                
            case "long" : case "java.lang.Long" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLong(Bytes.copy(bytes, idx + offset, 8, le)));
                
            case "float" : case "java.lang.Float" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloat(Bytes.copy(bytes, idx + offset, 4, le)));
                
            case "double" : case "java.lang.Double" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDouble(Bytes.copy(bytes, idx + offset, 8, le)));
            
            // basic array and list
            case "byte[]" : 
                return (bytes, idx, val) -> method.invoke(val, Arrays.copyOfRange(bytes, idx + offset, idx + offset + arrayLength));
            case "java.lang.Byte[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Arrays.copyOfRange(bytes, idx + offset, idx + offset + arrayLength)));
            
            case "short[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShortArray(Bytes.copy(bytes, idx + offset, arrayLength * 2, le), 0, arrayLength));
            case "java.lang.Short[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toShortArray(Bytes.copy(bytes, idx + offset, arrayLength * 2, le), 0, arrayLength)));
            case "java.util.List<java.lang.Short>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toShortList(Bytes.copy(bytes, idx + offset, arrayLength * 2, le), 0, arrayLength));
            
            case "int[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toIntArray(Bytes.copy(bytes, idx + offset, arrayLength * 4, le), 0, arrayLength));
            case "java.lang.Integer[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toIntArray(Bytes.copy(bytes, idx + offset, arrayLength * 4, le), 0, arrayLength)));
            case "java.util.List<java.lang.Integer>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toIntegerList(Bytes.copy(bytes, idx + offset, arrayLength * 4, le), 0, arrayLength));
                
            case "long[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLongArray(Bytes.copy(bytes, idx + offset, arrayLength * 8, le), 0, arrayLength));
            case "java.lang.Long[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toLongArray(Bytes.copy(bytes, idx + offset, arrayLength * 8, le), 0, arrayLength)));
            case "java.util.List<java.lang.Long>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toLongList(Bytes.copy(bytes, idx + offset, arrayLength * 8, le), 0, arrayLength));
            
            case "float[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloatArray(Bytes.copy(bytes, idx + offset, arrayLength * 4, le), 0, arrayLength));
            case "java.lang.Float[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toFloatArray(Bytes.copy(bytes, idx + offset, arrayLength * 4, le), 0, arrayLength))); 
            case "java.util.List<java.lang.Float>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toFloatList(Bytes.copy(bytes, idx + offset, arrayLength * 4, le), 0, arrayLength));
            
            case "double[]" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDoubleArray(Bytes.copy(bytes, idx + offset, arrayLength * 8, le), 0, arrayLength));
            case "java.lang.Double[]" : 
                return (bytes, idx, val) -> method.invoke(val, (Object)Converter.toUnPrimitive(Bytes.toDoubleArray(Bytes.copy(bytes, idx + offset, arrayLength * 8, le), 0, arrayLength)));
            case "java.util.List<java.lang.Double>" : 
                return (bytes, idx, val) -> method.invoke(val, Bytes.toDoubleList(Bytes.copy(bytes, idx + offset, arrayLength * 8, le), 0, arrayLength));
            
            // there is not contained a basic types 
            default :
                
                if (genericParameterTypeName.endsWith("[]")) {
                    
                    // -- @FixedDataClass[]
                    Class<?> componentType = parameterTypeClass.getComponentType();
                    // type check : support only the FixedDataClass
                    if (componentType.getDeclaredAnnotation(FixedDataClass.class) != null) {
                        FixedData fd = FixedData.getInstance(componentType);
                        int size = fd.meta().size();
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
                        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(genericParameterTypeName.substring(genericParameterTypeName.indexOf('<') + 1, genericParameterTypeName.lastIndexOf('>')));
                        if (clazz.getDeclaredAnnotation(FixedDataClass.class) != null) {
                            FixedData fd = FixedData.getInstance(clazz);
                            int size = fd.meta().size();
                            return (bytes, idx, val) -> {
                                List<?> list = new ArrayList<>();
                                for (int i = 0 ; i < arrayLength ; i++) {
                                    list.add(fd.toClass(bytes, idx + offset + (size * i)));
                                }
                                method.invoke(val, list);
                            };
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    
                } else if (parameterTypeClass.getDeclaredAnnotation(FixedDataClass.class) != null) {
                    
                    // -- @FixedDataClass
                    FixedData fd = FixedData.getInstance(parameterTypeClass);
                    return (bytes, idx, val) -> method.invoke(val, fd.<Object>toClass(bytes, idx + offset));
                    
                }
        }
        throw new IllegalArgumentException("does not support parameter type of void " + method.getName() + "("+genericParameterTypeName+") in the " + parentClassName);
    }

}
