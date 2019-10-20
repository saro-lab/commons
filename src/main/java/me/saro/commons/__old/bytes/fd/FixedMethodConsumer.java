package me.saro.commons.__old.bytes.fd;

import java.lang.reflect.InvocationTargetException;

/**
 * FixedMethodConsumer
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
@FunctionalInterface
public interface FixedMethodConsumer {
    void to(byte[] bytes, int idx, Object val) throws InvocationTargetException, IllegalAccessException;
}
