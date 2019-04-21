package me.saro.commons.bytes.fd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public interface FixedMethod {
    FixedMethodConsumer toByte(Class<?> genericParameterType, Method method);
    FixedMethodConsumer toClass(Class<?> genericReturnType, Method method) throws InvocationTargetException, IllegalAccessException;
}
