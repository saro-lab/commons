package me.saro.commons.lambdas;

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
 * 1.0.0
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
