package me.saro.commons;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import me.saro.commons.function.ThrowableBiConsumer;
import me.saro.commons.function.ThrowableBiFunction;
import me.saro.commons.function.ThrowableConsumer;
import me.saro.commons.function.ThrowableFunction;
import me.saro.commons.function.ThrowableRunnable;
import me.saro.commons.function.ThrowableSupplier;

/**
 * Lambdas Util
 * @author		PARK Yong Seo
 * @since		0.1
 */
public class Lambdas {
    private Lambdas() {
    }

    /**
     * throws Exception lambda to throws RuntimeException lambda
     * @param runnable
     * @return
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
     * throws Exception lambda to throws RuntimeException lambda
     * @param supplier
     * @return
     */
    public static <R> Supplier<R> runtime(ThrowableSupplier<R> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * throws Exception lambda to throws RuntimeException lambda
     * @param consumer
     * @return
     */
    public static <T> Consumer<T> runtime(ThrowableConsumer<T> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * throws Exception lambda to throws RuntimeException lambda
     * @param biConsumer
     * @return
     */
    public static <T, U> BiConsumer<T, U> runtime(ThrowableBiConsumer<T, U> biConsumer) {
        return (t, u) -> {
            try {
                biConsumer.accept(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * throws Exception lambda to throws RuntimeException lambda
     * @param function
     * @return
     */
    public static <T, R> Function<T, R> runtime(ThrowableFunction<T, R> function) {
        return t -> {
            try {
                return function.apply(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * throws Exception lambda to throws RuntimeException lambda
     * @param biFunction
     * @return
     */
    public static <T, U, R> BiFunction<T, U, R> runtime(ThrowableBiFunction<T, U, R> biFunction) {
        return (t, u) -> {
            try {
                return biFunction.apply(t, u);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
