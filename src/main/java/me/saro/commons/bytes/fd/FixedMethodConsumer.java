package me.saro.commons.bytes.fd;

import java.lang.reflect.InvocationTargetException;

/**
 * FixedMethodConsumer
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
@FunctionalInterface
public interface FixedMethodConsumer {
    void toClass(byte[] bytes, int idx, Object val) throws InvocationTargetException, IllegalAccessException;
}
