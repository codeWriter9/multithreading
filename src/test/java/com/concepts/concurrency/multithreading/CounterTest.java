package com.concepts.concurrency.multithreading;

import static com.concepts.concurrency.multithreading.RunnableFactory.consumerRunners;
import static com.concepts.concurrency.multithreading.RunnableFactory.incrementerRunners;
import static com.concepts.concurrency.multithreading.RunnableFactory.shutdownWithGrace;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.ExecutorService;

import org.junit.Test;
import org.slf4j.Logger;

/**
 * 
 * Checks whether the counter is thread safe
 * 
 * @author Sanjay Ghosh
 *
 */
public class CounterTest {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	/**
	 * 
	 * Tests whether the counter is thread safe
	 * 
	 */
	@Test
	public void testIncrementersAndConsumers() {
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
	 * Tests if separate counters in loop count to the same result
	 * 
	 */
	@Test
	public void testStartConsumersFirstAndThenIncrementers() {
		ExecutorService service = newCachedThreadPool();
		final Counter counter = new Counter();
		service.execute(consumerRunners(counter, c -> c.intValue() % 2 == 0));
		for (int loop = 0; loop < 10; loop++) {
			service.execute(incrementerRunners(counter));
		}
		shutdownWithGrace(service);
		assertEquals(Integer.valueOf(10), counter.intValue());
	}

	/**
	 * 
	 * Tests if separate counters in loop count to the same result
	 * 
	 */
	@Test
	public void testIncrementersAndConsumersInnerLoop() {
		ExecutorService service = newCachedThreadPool();
		Counter counter = new Counter();
		for (int loop = 0; loop < 10; loop++) {
			service.execute(incrementerRunners(counter, 10));
		}
		service.execute(consumerRunners(counter));
		shutdownWithGrace(service);
		assertEquals(Integer.valueOf(100), counter.intValue());
	}

	/**
	 * 
	 * Tests if separate counters in loop with steps count to the same result 
	 * 
	 */
	@Test
	public void testIncrementersAndConsumersInnerLoopAndStep() {
		ExecutorService service = newCachedThreadPool();
		Counter counter = new Counter();
		for (int loop = 0; loop < 10; loop++) {
			service.execute(incrementerRunners(counter, 10, loop % 2 == 0 ? 1 : 2));
		}
		service.execute(consumerRunners(counter));
		shutdownWithGrace(service);
		assertEquals(Integer.valueOf(100), counter.intValue());
	}
}