package me.saro.commons.__old.bytes.fd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.SneakyThrows;
import me.saro.commons.__old.bytes.fd.annotations.BinaryData;
import me.saro.commons.__old.bytes.fd.annotations.DateData;
import me.saro.commons.__old.bytes.fd.annotations.FixedDataClass;
import me.saro.commons.__old.bytes.fd.annotations.TextData;

/**
 * FixedMethodExtractor
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public class FixedMethodUtils {
    
    public static List<FixedMethodConsumer> toBytesConsumers(FixedDataClass fixedDataClassInfo, final Class<?> clazz, final Field[] fields) {
        List<FixedMethodConsumer> list = new ArrayList<FixedMethodConsumer>();
        for (Field field : fields) {
            FixedMethod fixedMethod = getFixedMethod(fixedDataClassInfo, clazz, field);
            Method method;
            if (fixedMethod != null && (method = getMethod(clazz, field, "get")) != null) {
                list.add(fixedMethod.toBytes(method));
            }
            
        }
        return list;
    }
    
    @SneakyThrows
    public static List<FixedMethodConsumer> toClassConsumers(FixedDataClass fixedDataClassInfo, final Class<?> clazz, final Field[] fields) {        
        List<FixedMethodConsumer> list = new ArrayList<FixedMethodConsumer>();
        for (Field field : fields) {
            FixedMethod fixedMethod = getFixedMethod(fixedDataClassInfo, clazz, field);
            Method method;
            if (fixedMethod != null && (method = getMethod(clazz, field, "set")) != null) {
                list.add(fixedMethod.toClass(method));
            }
            
        }
        return list;
    }
    
    private static FixedMethod getFixedMethod(FixedDataClass fixedDataClassInfo, final Class<?> clazz, final Field field) {
        int cnt = 0;
        List<Annotation> annotations = 
                Arrays.asList(field.getDeclaredAnnotation(TextData.class), field.getDeclaredAnnotation(BinaryData.class), field.getDeclaredAnnotation(DateData.class));
        Annotation dataType = null;
        for (Annotation annotation : annotations) {
            if (annotation != null) {
                cnt++;
                dataType = annotation;
            }
        }
        if (cnt == 1) {
            switch (dataType.annotationType().getSimpleName()) {
                case "BinaryData" :
                    return new FixedMethodBinaryType(fixedDataClassInfo, clazz.getName(), (BinaryData)dataType);
                case "TextData" :
                    return new FixedMethodTextType(fixedDataClassInfo, clazz.getName(), (TextData)dataType);
                case "DateData" :
                    return new FixedMethodDateType(fixedDataClassInfo, clazz.getName(), (DateData)dataType);
            }
        } else if (cnt > 1) {
            throw new IllegalArgumentException(
                "one data field must have one data field annotation but " + clazz.getName() + "." + field.getName() +
                " have many annotations : " +
                annotations.stream().map(e -> "@" + e.annotationType().getName()).collect(Collectors.joining(", "))
            );
        }
        return null;
    }
    
    private static Method getMethod(final Class<?> clazz, final Field field, String type) {
        String fieldName = field.getName();
        String methodName = type +  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        List<Method> methods = Stream
            .of(clazz.getDeclaredMethods())
            .filter(e -> e.getName().equals(methodName))
            .filter(e -> 
                (type.equals("get") && e.getParameterCount() == 0 && !e.getReturnType().getTypeName().toString().equals("void")) ||
                (type.equals("set") && e.getParameterCount() == 1)
            ).collect(Collectors.toList());
        if (methods.size() > 1) {
            throw new IllegalArgumentException(
                "duplicate method name : " + (
                    methods.stream()
                        .map(e -> e.getReturnType().toString() + " " + e.getName() + "("+(type.equals("get") ? "" : e.getGenericParameterTypes()[0].getTypeName())+")")
                        .collect(Collectors.joining(", ")
                )) + " in class" + clazz.getName()
            );
        }
        return methods.size() == 1 ? methods.get(0) : null;
    }
}
