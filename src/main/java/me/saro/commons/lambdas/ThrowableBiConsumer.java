package me.saro.commons.lambdas;

/**
 * Throwable <code>BiConsumer</code>
 * 
 * @param <T>
 * input parameter type T
 * 
 * @param <U>
 * input parameter type U
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
public interface ThrowableBiConsumer<T, U> {
	/**
	 * @see
	 * java.util.function.Consumer
	 * 
	 * @param <T>
	 * input parameter type T
	 * 
	 * @param <U>
	 * input parameter type U
	 * 
	 * @throws Exception
	 */
	void accept(T t, U u) throws Exception;
}
