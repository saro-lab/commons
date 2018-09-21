package me.saro.commons;

import org.junit.Test;

import me.saro.commons.Lambdas.*;

public class LambdasTest {
	
	@Test
    public void testLambdas() {
		
		
    }
	
	public boolean test(ThrowableSupplier<String> throwableSupplier) throws Exception {
		throwableSupplier.get();
		return true;
	}
	
	public boolean test(ThrowableRunnable throwableRunnable) throws Exception {
		throwableRunnable.run();
		return true;
	}
	
	public boolean test(ThrowableConsumer<String> throwableConsumer) throws Exception {
		throwableConsumer.accept("P1");
		return true;
	}
	
	public boolean test(ThrowableBiConsumer<String, String> throwableBiConsumer) throws Exception {
		throwableBiConsumer.accept("P1", "P2");
		return true;
	}
	
	public boolean test(ThrowableTriConsumer<String, String, String> throwableTriConsumer) throws Exception {
		throwableTriConsumer.accept("P1", "P2", "P3");
		return true;
	}
	
	public boolean test(ThrowableFunction<String, String> throwableFunction) throws Exception {
		throwableFunction.apply("P1");
		return true;
	}
	
	public boolean test(ThrowableBiFunction<String, String, String> throwableBiFunction) throws Exception {
		throwableBiFunction.apply("P1", "P2");
		return true;
	}
	
	public boolean test(ThrowableTriFunction<String, String, String, String> throwableTriFunction) throws Exception {
		throwableTriFunction.apply("P1", "P2", "P3");
		return true;
	}
}
