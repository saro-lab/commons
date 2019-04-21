package me.saro.commons.bytes.fd;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public interface FixedMethod {
    FixedMethodConsumer toBytes(Type genericReturnType, Method method);
    FixedMethodConsumer toClass(Type genericParameterType, Method method);
}
