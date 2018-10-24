package com.concepts.concurrency.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import com.concepts.concurrency.multithreading.color.ColorPrinter;
import com.concepts.concurrency.multithreading.color.Status;

/**
 * 
 * Static Factory class to create Runnables
 * 
 * @author Sanjay Ghosh
 *
 */
public class RunnableFactory {

	/**
	 * 
	 * Creates a runnable which will wait until the status is not of the current color printer
	 * Once that has printed that will go to the next color and notify all threads waiting 
	 * 
	 * @param colorPrinter
	 * @param status
	 * @return
	 */
	public static Runnable colorPrinterWithStatusRunners(final ColorPrinter colorPrinter, final Status status) {
		return () -> {
			synchronized (status) {
				try {
					while (status.isNot(colorPrinter.color())) {
						status.wait();
					}
					System.out.println(colorPrinter.color());
					status.set(ColorPrinter.nextColor(colorPrinter));
					status.notifyAll();
				} catch (InterruptedException e) {
					System.err.println(e.getMessage());
				}
			}
		};
	}
	
	/**
	 * 
	 * Create a runnable which will increment the counter and print the same
	 * 
	 * @param counter
	 * @return
	 */
	public static Runnable incrementerRunners(final Counter counter) {
		return () -> {
			counter.incrementAndIntValue();			
		};
	}
	
	/**
	 * 
	 * Create a runnable which will consume the counter and print the same
	 * 
	 * @param counter
	 * @return
	 */
	public static Runnable consumerRunners(final Counter counter) {
		return () -> {
			System.out.println(counter.intValue());
		};
	}
	
	/**
	 * 
	 * Create a runnable which will consume the counter and print the same
	 * 
	 * @param counter
	 * @return
	 */
	public static Runnable consumerRunners(final Counter counter, final Predicate<Counter> predicate) {
		return () -> {
			if(predicate.test(counter)) {
				System.out.println(counter.intValue());
			}
			else {
				while(!predicate.test(counter)) {
					counter.waitOnLock();
				}
			}
		};
	}
	
	/**
	 * 
	 * Boiler plate code to shutdown with grace Ideally this should be in some
	 * abstract class
	 * 
	 */
	public static void shutdownWithGrace(final ExecutorService service) {
		try {
			service.shutdown();
			while (!service.awaitTermination(1000, TimeUnit.MILLISECONDS))
				service.shutdownNow();
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
		}
	}

}
