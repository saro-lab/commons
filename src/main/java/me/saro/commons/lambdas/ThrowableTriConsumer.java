package me.saro.commons.lambdas;

/**
 * Throwable <code>TriConsumer</code>
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
 * @see
 * java.util.function.Consumer
 * 
 * @author
 * PARK Yong Seo
 * 
 * @since
 * 1.0.0
 */
@FunctionalInterface
public interface ThrowableTriConsumer<T, U, V> {
	/**
	 * @see
	 * java.util.function.Consumer
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
	 * @throws Exception
	 */
	void accept(T t, U u, V v) throws Exception;
}
