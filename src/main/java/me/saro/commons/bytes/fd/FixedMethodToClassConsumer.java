package me.saro.commons.bytes.fd;

import java.lang.reflect.InvocationTargetException;

/**
 * FixedMethodToClassConsumer
 * @author      PARK Yong Seo
 * @since       4.0.0
 */
@FunctionalInterface
public interface FixedMethodToClassConsumer {
    void toClass(byte[] bytes, int idx, Object val) throws InvocationTargetException, IllegalAccessException;
}
