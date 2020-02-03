package com.concepts.concurrency.multithreading;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

import com.concepts.concurrency.multithreading.prime.PrimeGenerator;

public class PrimeGeneratorTest {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	@Test
	public void testPrimeGenerators() {
		PrimeGenerator primeGenerator = new PrimeGenerator(20000, null);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" Primes " + primeGenerator.primes());
	}
	
	@Test
	public void testPrimeGeneratorsTiny() {
		PrimeGenerator primeGenerator = new PrimeGenerator(500, null);
		try {
			Thread.sleep(1000 / 2);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" Primes " + primeGenerator.primes());
	}

	@Test
	public void testPrimeGeneratorsLarge() {
		PrimeGenerator primeGenerator = new PrimeGenerator(1000000, null);
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" Primes " + primeGenerator.primes());
	}
}