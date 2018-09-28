package me.saro.commons.lambdas;

/**
 * Throwable <code>Consumer</code>
 * 
 * @param <T>
 * input parameter type T
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
public interface ThrowableConsumer<T> {
	/**
	 * @see
	 * java.util.function.Consumer
	 * 
	 * @param t
	 * input parameter type T
	 * 
	 * @throws Exception
	 */
	void accept(T t) throws Exception;
}
