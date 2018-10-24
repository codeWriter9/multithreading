package com.concepts.concurrency.multithreading;

import static com.concepts.concurrency.multithreading.RunnableFactory.colorPrinterWithStatusRunners;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.concepts.concurrency.multithreading.color.Color;
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
	

	/**
	 * Tests whether the colors are in sequence
	 */
	@Test
	public void checkColorsInSequence() {
		List<ColorPrinter> list = new ArrayList<ColorPrinter>();
		list.addAll(generate(ColorPrinter::redPrinter).limit(10).collect(toList()));
		list.addAll(generate(ColorPrinter::greenPrinter).limit(10).collect(toList()));
		list.addAll(generate(ColorPrinter::bluePrinter).limit(10).collect(toList()));

		Status status = new Status(Color.RED);
		Thread threads[] = new Thread[30];
		int index = 0;
		for (ColorPrinter printer : list) {
			threads[index++] = new Thread(colorPrinterWithStatusRunners(printer, status));
		}

		for (int i = 0; i < 10 * 3; i++) {
			threads[i].start();
		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			System.err.println(e1.getMessage());
		}

		for (int i = 0; i < 10 * 3; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
	}

}
