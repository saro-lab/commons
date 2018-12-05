package me.saro.commons;

import me.saro.commons.function.ThrowableRunnable;

/**
 * test class
 * @author      PARK Yong Seo
 * @since       1.0
 */
public class Tests {
    private Tests() {
    }
    
    /**
     * timestamp<br>
     * check time of the runnable
     * @param runnable
     * @return
     * timeMillis
     */
    public static long timestamp(ThrowableRunnable runnable) {
        long ts = System.currentTimeMillis();
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ts = System.currentTimeMillis() - ts;
        System.out.println("timestamp : " + ts);
        return ts;
    }
}
