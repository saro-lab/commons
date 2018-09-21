package saro.commons;

/**
 * Lambda
 * @author		PARK Yong Seo
 * @since		1.0.0
 */
public class Lambdas {
	
    private Lambdas() {
    }

    public interface ThrowableSupplier<T> {
        T get() throws Exception;
    }

    public interface ThrowableRunnable {
        void run() throws Exception;
    }
    
    public interface ThrowableConsumer<T> {
        public void accept(T t) throws Exception;
    }
    public interface ThrowableBiConsumer<T, U> {
        public void accept(T t, U u) throws Exception;
    }
    
    public interface ThrowableTriConsumer<T, U, V> {
        public void accept(T t, U u, V v) throws Exception;
    }

    public interface ThrowableFunction<T, R> {
        public R apply(T t) throws Exception;
    }
    
    public interface ThrowableBiFunction<T, U, R> {
        public R apply(T t, U u) throws Exception;
    }

    public interface ThrowableTriFunction<T, U, V, R> {
        public R apply(T t, U u, V v) throws Exception;
    }
}
