package me.saro.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.function.Function;

import org.junit.Test;

import me.saro.commons.lambdas.*;

public class LambdasTest {

	@Test
	public void testLambdas() throws Exception {

		// Runnable
		throwableRunnable(() -> assertTrue(true));

		// Supplier
		assertEquals("1", throwableSupplier(() -> "1"));

		// Consumers
		throwableConsumer(a -> assertEquals("1", a), "1");
		throwableBiConsumer((a, b) -> assertEquals("12", a + b), "1", "2");
		throwableTriConsumer((a, b, c) -> assertEquals("123", a + b + c), "1", "2", "3");

		// Functions
		assertEquals("1", throwableFunction(a -> a, "1"));
		assertEquals("12", throwableBiFunction((a, b) -> a + b, "1", "2"));
		assertEquals("123", throwableTriFunction((a, b, c) -> a + b + c, "1", "2", "3"));


		assertEquals("OK TEST", callTest(Lambdas.runtime(p1 -> {
			Thread.sleep(100);
			return "OK " + p1;
		})));
		
	}

	public void throwableRunnable(ThrowableRunnable throwableRunnable) throws Exception {
		throwableRunnable.run();
	}

	public String throwableSupplier(ThrowableSupplier<String> throwableSupplier) throws Exception {
		return throwableSupplier.get();
	}

	public void throwableConsumer(ThrowableConsumer<String> throwableConsumer, String p1) throws Exception {
		throwableConsumer.accept(p1);
	}

	public void throwableBiConsumer(ThrowableBiConsumer<String, String> throwableBiConsumer, String p1, String p2) throws Exception {
		throwableBiConsumer.accept(p1, p2);
	}

	public void throwableTriConsumer(ThrowableTriConsumer<String, String, String> throwableTriConsumer, String p1, String p2, String p3) throws Exception {
		throwableTriConsumer.accept(p1, p2, p3);
	}

	public String throwableFunction(ThrowableFunction<String, String> throwableFunction, String p1) throws Exception {
		return throwableFunction.apply(p1);
	}

	public String throwableBiFunction(ThrowableBiFunction<String, String, String> throwableBiFunction, String p1, String p2) throws Exception {
		return throwableBiFunction.apply(p1, p2);
	}

	public String throwableTriFunction(ThrowableTriFunction<String, String, String, String> throwableTriFunction, String p1, String p2, String p3) throws Exception {

		return throwableTriFunction.apply(p1, p2, p3);
	}

	public String callTest(Function<String, String> function) {
		return function.apply("TEST");
	}
}
