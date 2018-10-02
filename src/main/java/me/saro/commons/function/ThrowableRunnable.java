package me.saro.commons.function;

/**
 * Throwable <code>Runnable</code>
 * 
 * @see
 * java.lang.Runnable
 * 
 * @author
 * PARK Yong Seo
 * 
 * @since
 * 0.1
 */
@FunctionalInterface
public interface ThrowableRunnable {
	/**
	 * @see
	 * java.lang.Runnable
	 * 
	 * @throws
	 * Exception
	 */
	void run() throws Exception;
}
