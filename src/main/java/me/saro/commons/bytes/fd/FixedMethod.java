package me.saro.commons.bytes.fd;

import java.lang.reflect.Method;

/**
 * FixedField
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
public interface FixedMethod {
    FixedMethodConsumer toBytes(Method method);
    FixedMethodConsumer toClass(Method method);
}
