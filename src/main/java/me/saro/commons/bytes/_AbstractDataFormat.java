package me.saro.commons.bytes;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * AbstractData
 * @author      PARK Yong Seo
 * @since       1.4
 */
abstract class _AbstractDataFormat {
    /**
     * getter
     * @param clazz
     * @param fieldName
     * @return
     */
    protected Optional<Method> getter(Class<?> clazz, String fieldName, boolean ignoreNotFound) {
        String methodNameGet = "get" + (Character.toUpperCase(fieldName.charAt(0))) + fieldName.substring(1);
        //String methodNameIs = "is" + (Character.toUpperCase(fieldName.charAt(0))) + fieldName.substring(1); boolean has not support
        List<Method> methods = Stream.of(clazz.getDeclaredMethods())
            //.filter(e -> e.getName().equals(methodNameGet) || e.getName().equals(methodNameIs))
            .filter(e -> e.getName().equals(methodNameGet))
            .filter(e -> !e.getReturnType().equals(Void.TYPE))
            .filter(e -> e.getParameterCount() == 0)
            .collect(Collectors.toList());
        
        if (methods.size() == 0) {
            if (ignoreNotFound) {
                return Optional.ofNullable(null);
            }
            throw new IllegalArgumentException("not found getter of "+fieldName+" field");
        }
        if (methods.size() > 1) {
            throw new IllegalArgumentException(methodNameGet + "() is ambiguous");
        }
        
        return Optional.of(methods.get(0));
    }
    
    /**
     * setter
     * @param clazz
     * @param fieldName
     * @return
     */
    protected Optional<Method> setter(Class<?> clazz, String fieldName, boolean ignoreNotFound) {
        String methodName = "set" + (Character.toUpperCase(fieldName.charAt(0))) + fieldName.substring(1);
        List<Method> methods = Stream.of(clazz.getDeclaredMethods())
            .filter(e -> e.getName().equals(methodName))
            .filter(e -> e.getParameterCount() == 1)
            .collect(Collectors.toList());
        
        if (methods.size() == 0) {
            if (ignoreNotFound) {
                return Optional.ofNullable(null);
            }
            throw new IllegalArgumentException("not found setter of "+fieldName+" field");
        }
        if (methods.size() > 1) {
            throw new IllegalArgumentException(methodName + "() is ambiguous");
        }
        
        return Optional.of(methods.get(0));
    }
}
