package me.saro.commons.bytes.fd;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public interface FixedMethod {
    FixedMethodConsumer toByte(Class<?> parameterType, final String genericParameterType, String methodName);
    FixedMethodConsumer toClass(Class<?> returnType, final String genericReturnType, Method method) throws InvocationTargetException, IllegalAccessException;
}
