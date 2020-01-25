package com.concepts.concurrency.multithreading;

import static com.concepts.concurrency.multithreading.CallableFactory.colorPrinterWithStatusCallers;
import static com.concepts.concurrency.multithreading.RunnableFactory.colorPrinterWithStatusRunners;
import static com.concepts.concurrency.multithreading.color.Color.RED;
import static java.lang.System.err;
import static java.lang.Thread.sleep;
import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Test;
import org.slf4j.Logger;

import com.concepts.concurrency.multithreading.color.ColorPrinter;
import com.concepts.concurrency.multithreading.color.Status;

/**
 * 
 * Color Printer Test which can check if the RGB colors each printed by
 * different color printer are in sequence
 * 
 * @author Sanjay Ghosh
 *
 */
public class ColorTest {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	/**
	 * Tests whether the colors are in sequence
	 */
	@Test
	public void checkColorsInSequence() {
		List<ColorPrinter> list = new ArrayList<ColorPrinter>();
		list.addAll(generate(ColorPrinter::redPrinter).limit(10).collect(toList()));
		list.addAll(generate(ColorPrinter::greenPrinter).limit(10).collect(toList()));
		list.addAll(generate(ColorPrinter::bluePrinter).limit(10).collect(toList()));

		Status status = new Status(RED);
		Thread threads[] = new Thread[30];
		int index = 0;
		for (ColorPrinter printer : list) {
			threads[index++] = new Thread(colorPrinterWithStatusRunners(printer, status));
		}

		for (int i = 0; i < 10 * 3; i++) {
			threads[i].start();
		}

		try {
			sleep(5000);
		} catch (InterruptedException e1) {
			LOG.error(e1.getMessage(), e1);
		}

		for (int i = 0; i < 10 * 3; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Tests whether the colors are in sequence
	 */
	@Test
	public void checkColorsInSequenceThroughExecutorService() {
		List<ColorPrinter> list = new ArrayList<ColorPrinter>();
		list.addAll(generate(ColorPrinter::redPrinter).limit(10).collect(toList()));
		list.addAll(generate(ColorPrinter::greenPrinter).limit(10).collect(toList()));
		list.addAll(generate(ColorPrinter::bluePrinter).limit(10).collect(toList()));

		Status status = new Status(RED);

		ExecutorService executorService = newCachedThreadPool();
		List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		for (ColorPrinter printer : list) {
			futures.add(executorService.submit(colorPrinterWithStatusCallers(printer, status)));
		}

		try {
			sleep(5000);
		} catch (InterruptedException e1) {
			err.println(e1.getMessage());
		}

		for (int i = 0; i < 10 * 3; i++) {
			try {
				assertTrue(futures.get(i).get());
			} catch (ExecutionException | InterruptedException e) {
				err.println(e.getMessage());
			}
		}
	}
}