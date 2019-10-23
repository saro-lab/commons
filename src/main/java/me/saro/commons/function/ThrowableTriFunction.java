package me.saro.commons.function;

/**
 * Throwable <code>TriFunction</code>
 * 
 * @param <T>
 * input parameter type T
 * 
 * @param <U>
 * input parameter type U
 * 
 * @param <V> 
 * input parameter type V
 * 
 * @param <R> 
 * output return type
 * 
 * @see
 * java.util.function.Function
 * 
 * @author
 * PARK Yong Seo
 * 
 * @since
 * 0.1
 */
@FunctionalInterface
public interface ThrowableTriFunction<T, U, V, R> {
    /**
     * @see
     * java.util.function.Function
     * 
     * @param t
     * input parameter type T
     * 
     * @param u
     * input parameter type U
     * 
     * @param v 
     * input parameter type V
     * 
     * @return
     * output return
     * 
     * @throws Exception
     */
    R apply(T t, U u, V v) throws Exception;
}
