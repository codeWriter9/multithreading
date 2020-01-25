package com.concepts.concurrency.multithreading;

import static com.concepts.concurrency.multithreading.RunnableFactory.consumerRunners;
import static com.concepts.concurrency.multithreading.RunnableFactory.incrementerRunners;
import static com.concepts.concurrency.multithreading.RunnableFactory.shutdownWithGrace;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;

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
	 * 
	 * Tests whether the counter is thread safe
	 */
	@Test
	public void testIncrementAndConsumeCount() {
		ExecutorService service = newCachedThreadPool();
		Counter counter = new Counter();
		for (int loop = 0; loop < 10; loop++) {
			service.execute(incrementerRunners(counter));
		}
		service.execute(consumerRunners(counter));		
		shutdownWithGrace(service);
		assertEquals(Integer.valueOf(10), counter.intValue());		
	}

	/**
	 * 
	 * Tests whether the counter is thread safe
	 */
	@Test
	public void testCounterIsThreadSafe2() {
		ExecutorService service = newCachedThreadPool();
		final Counter counter = new Counter();
		service.execute(consumerRunners(counter, c -> c.intValue() % 2 == 0));
		for (int loop = 0; loop < 10; loop++) {
			service.execute(incrementerRunners(counter));
		}		
		shutdownWithGrace(service);
		assertEquals(Integer.valueOf(10), counter.intValue());
	}
}