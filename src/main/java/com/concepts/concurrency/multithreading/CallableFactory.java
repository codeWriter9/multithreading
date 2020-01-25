package com.concepts.concurrency.multithreading;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.Callable;

import org.slf4j.Logger;

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

	private static final Logger LOG = getLogger(lookup().lookupClass());

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
					LOG.info(colorPrinter.color().toString());
					status.set(ColorPrinter.nextColor(colorPrinter));
					status.notifyAll();
					return TRUE;
				} catch (InterruptedException e) {
					LOG.error(e.getMessage(), e);
					return FALSE;
				}
			}
		};
	}

}
