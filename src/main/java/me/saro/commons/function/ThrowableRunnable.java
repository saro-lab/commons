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
    
    /**
     * throws Exception lambda to throws RuntimeException lambda
     * @param runnable
     * @return
     * @since
     * 0.6
     */
    public static Runnable runtime(ThrowableRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
    
    /**
     * ignore exception<br>
     * this method recommend only special situation 
     * @param runnable
     * @return
     * @since
     * 1.1
     */
    public static Runnable ignore(ThrowableRunnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
            }
        };
    }
}
