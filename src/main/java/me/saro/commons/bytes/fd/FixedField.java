package me.saro.commons.bytes.fd;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;
import me.saro.commons.bytes.fd.annotations.BinaryData;
import me.saro.commons.bytes.fd.annotations.DateData;
import me.saro.commons.bytes.fd.annotations.TextData;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       3.1.0
 */
@Getter
class FixedField {
    
    FixedField(Class<?> clazz, Field field) {
        this.clazz = clazz;
        this.field = field;
        this.text = field.getDeclaredAnnotation(TextData.class);
        this.binary = field.getDeclaredAnnotation(BinaryData.class);
        this.date = field.getDeclaredAnnotation(DateData.class);
        init();
    }
    
    final Class<?> clazz;
    final Field field;
    
    Annotation type;
    final TextData text;
    final BinaryData binary;
    final DateData date;
    
    Method getMethod;
    Method setMethod;
    
    void init() {
        // check annotation
        List<Annotation> types = Stream.of(text, binary, date).filter(e -> e != null).collect(Collectors.toList());
        
        if (types.size() == 1) {
            // OK : have one data field annotation
            this.type = types.get(0);
        } else if (types.size() > 1) {
            // ERROR : have many data field annotations
            throw new IllegalArgumentException(
                "one data field must have one data field annotation but " + clazz.getName() + "." + field.getName() +
                " have many annotations : " +
                types.stream().map(e -> "@" + e.annotationType().getName()).collect(Collectors.joining(", "))
            );
        }
        
        String methodPostName = field.getName();
        methodPostName = methodPostName.substring(0, 1).toUpperCase() + methodPostName.substring(1);
        String getMethodName = "get" + methodPostName;
        String setMethodName = "set" + methodPostName;
        
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(getMethodName)) {
                if (method.getParameterCount() == 0 && !method.getGenericReturnType().getTypeName().equals("void")) {
                    if (getMethod != null) {
                        throw new IllegalArgumentException(
                            "duplicate method name " + clazz.getName() + "." + getMethodName + "()"
                        );
                    }
                    getMethod = method;
                }
            } else if (method.getName().equals(setMethodName)) {
                if (method.getParameterCount() == 1) {
                    if (setMethod != null) {
                        throw new IllegalArgumentException(
                            "duplicate method name " + clazz.getName() + "." + setMethodName + "(...)"
                        );
                    }
                    setMethod = method;
                }
            }
        }
    }
    
    /**
     * check is reflection target
     * @return
     */
    public boolean isReflectionTarget() {
        return type != null && (getMethod != null || setMethod != null);
    }
}
