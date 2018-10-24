package com.concepts.concurrency.multithreading;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.System.err;
import static java.lang.System.out;

import java.util.concurrent.Callable;

import com.concepts.concurrency.multithreading.color.ColorPrinter;
import com.concepts.concurrency.multithreading.color.Status;

/**
 * 
 * static Factory to create Callables defined as per GoF with the product
 * 
 * interface which in this case happens to Callable and supplied by Java
 * platform and creator interface which is this static class
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class CallableFactory {

	/**
	 * 
	 * Creates a callable which will wait until the status is not of the current
	 * color printer Once that has printed that will go to the next color and notify
	 * all threads waiting
	 * 
	 * @param colorPrinter
	 * @param status
	 * @return Callable
	 */
	public static Callable<Boolean> colorPrinterWithStatusCallers(final ColorPrinter colorPrinter,
			final Status status) {
		return () -> {
			synchronized (status) {
				try {
					while (status.isNot(colorPrinter.color())) {
						status.wait();
					}
					out.println(colorPrinter.color());
					status.set(ColorPrinter.nextColor(colorPrinter));
					status.notifyAll();
					return TRUE;
				} catch (InterruptedException e) {
					err.println(e.getMessage());
					return FALSE;
				}
			}
		};
	}

}
