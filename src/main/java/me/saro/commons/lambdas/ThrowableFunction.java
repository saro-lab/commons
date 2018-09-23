package me.saro.commons.lambdas;

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
 * 1.0.0
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
