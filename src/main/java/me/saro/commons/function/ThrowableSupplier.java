package me.saro.commons.function;

/**
 * Throwable <code>Supplier</code>
 * 
 * @param <R> 
 * output return type
 * 
 * @see
 * java.util.function.Supplier
 * 
 * @author
 * PARK Yong Seo
 * 
 * @since
 * 1.0.0
 */
@FunctionalInterface
public interface ThrowableSupplier<R> {
	/**
	 * @see
	 * java.util.function.Supplier
	 * 
	 * @return
	 * output return
	 * 
	 * @throws
	 * Exception
	 */
	R get() throws Exception;
}
