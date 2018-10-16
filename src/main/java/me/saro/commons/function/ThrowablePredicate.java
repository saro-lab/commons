package me.saro.commons.function;

/**
 * Throwable <code>Predicate</code>
 * 
 * @param <T>
 * input parameter type T
 * 
 * @see
 * java.util.function.Predicate
 * 
 * @author
 * PARK Yong Seo
 * 
 * @since
 * 0.1
 */
@FunctionalInterface
public interface ThrowablePredicate<T> {
    /**
     * @see
     * java.util.function.Predicate
     * 
     * @param t
     * input parameter type T
     * 
     * @return
     * test return
     * 
     * @throws Exception
     */
    boolean test(T t) throws Exception;
}
