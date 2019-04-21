package me.saro.commons.bytes.fd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public interface FixedMethod {
    FixedMethodConsumer toByte(Type genericParameterType, Method method);
    FixedMethodConsumer toClass(Type genericReturnType, Method method) throws InvocationTargetException, IllegalAccessException;
}
