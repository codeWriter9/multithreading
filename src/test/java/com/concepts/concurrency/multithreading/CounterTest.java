package com.concepts.concurrency.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

/**
 * 
 * Checks whether the counter is thread safe
 * 
 * @author Sanjay Ghosh
 *
 */
public class CounterTest {
	
	/**
	 * Tests whether the counter is thread safe
	 */
	@Test
	public void testCounterIsThreadSafe() {
		ExecutorService service = Executors.newCachedThreadPool();
		Counter counter = new Counter();
		for(int loop=0;loop<10;loop++) service.execute(RunnableFactory.incrementerRunners(counter));
		service.execute(RunnableFactory.consumerRunners(counter));
		RunnableFactory.shutdownWithGrace(service);
	}
	
	/**
	 * Tests whether the counter is thread safe
	 */
	@Test
	public void testCounterIsThreadSafe2() {
		ExecutorService service = Executors.newCachedThreadPool();
		final Counter counter = new Counter();
		service.execute(RunnableFactory.consumerRunners(counter, c -> c.intValue() % 2 == 0));
		for(int loop=0;loop<10;loop++) service.execute(RunnableFactory.incrementerRunners(counter));		
		RunnableFactory.shutdownWithGrace(service);
	}

}
