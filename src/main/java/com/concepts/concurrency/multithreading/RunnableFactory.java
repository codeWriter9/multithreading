package com.concepts.concurrency.multithreading;

import static java.lang.System.err;
import static java.lang.System.out;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

import com.concepts.concurrency.multithreading.color.ColorPrinter;
import com.concepts.concurrency.multithreading.color.Status;

/**
 * 
 * Static Factory class to create Runnables
 * 
 * interface which in this case happens to Callable and supplied by Java
 * platform and creator interface which is this static class
 * 
 * @author Sanjay Ghosh
 *
 */
public class RunnableFactory {

	/**
	 * 
	 * Creates a runnable which will wait until the status is not of the current
	 * color printer Once that has printed that will go to the next color and notify
	 * all threads waiting
	 * 
	 * @param colorPrinter
	 * @param status
	 * @return Runnable
	 */
	public static Runnable colorPrinterWithStatusRunners(final ColorPrinter colorPrinter, final Status status) {
		return () -> {
			synchronized (status) {
				try {
					while (status.isNot(colorPrinter.color())) {
						status.wait();
					}
					out.println(colorPrinter.color());
					status.set(ColorPrinter.nextColor(colorPrinter));
					status.notifyAll();
				} catch (InterruptedException e) {
					err.println(e.getMessage());
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
			out.println(counter.intValue());
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
			if (predicate.test(counter)) {
				out.println(counter.intValue());
			} else {
				while (!predicate.test(counter)) {
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
	 * TODO this method will be removed and a suitable utils library would be added
	 */
	public static void shutdownWithGrace(final ExecutorService service) {
		try {
			service.shutdown();// Stop taking any further request
			while (!service.awaitTermination(1000, MILLISECONDS)) {// wait for 1 sec
				service.shutdownNow()
						.forEach((runnable) -> System.out.println(" waiting for this to stop " + runnable.toString()));// force
																														// the
																														// running
																														// threads
																														// to
																														// stop
			}
		} catch (InterruptedException e) {
			err.println(e.getMessage());
		}
	}

}
