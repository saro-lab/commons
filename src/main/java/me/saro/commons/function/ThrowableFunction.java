package me.saro.commons.function;

/**
 * Throwable <code>Function</code>
 * 
 * @param <T>
 * input parameter type T
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
public interface ThrowableFunction<T, R> {
	/**
	 * @see
	 * java.util.function.Function
	 * 
	 * @param t
	 * input parameter type T
	 * 
	 * @return
	 * output return
	 * 
	 * @throws Exception
	 */
	R apply(T t) throws Exception;
}
