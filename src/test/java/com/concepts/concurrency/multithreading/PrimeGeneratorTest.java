package com.concepts.concurrency.multithreading;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.Test;
import org.slf4j.Logger;

import com.concepts.concurrency.multithreading.prime.PrimeGenerator;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class PrimeGeneratorTest {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	/**
	 * 
	 * 
	 */
	@Test
	public void testPrimeGenerators() {
		PrimeGenerator primeGenerator = new PrimeGenerator(100000, null);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" No. of Primes less than 100,000: " + primeGenerator.primes().size());
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void testPrimeGeneratorsTiny() {
		PrimeGenerator primeGenerator = new PrimeGenerator(10, null);
		try {
			Thread.sleep(1000 / 2);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" No. of Primes less than 10: " + primeGenerator.primes().size());
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void testPrimeGeneratorsTiny2() {
		PrimeGenerator primeGenerator = new PrimeGenerator(100, null);
		try {
			Thread.sleep(1000 / 2);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" No. of Primes less than 100: " + primeGenerator.primes().size());		
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void testPrimeGeneratorsSmall() {
		PrimeGenerator primeGenerator = new PrimeGenerator(1000, null);
		try {
			Thread.sleep(1000 / 2);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" No. of Primes less than 1,000: " + primeGenerator.primes().size());		
	}
	
	
	/**
	 * 
	 * 
	 * 
	 */
	@Test
	public void testPrimeGeneratorsRegular() {
		PrimeGenerator primeGenerator = new PrimeGenerator(10000, null);
		try {
			Thread.sleep(1000 / 2);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" No. of Primes less than 10,000: " + primeGenerator.primes().size());		
	}

	/**
	 * 
	 * 
	 */
	@Test
	public void testPrimeGeneratorsLarge() {
		PrimeGenerator primeGenerator = new PrimeGenerator(1000000, null);
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		}
		LOG.info(" No. of Primes less than 1000,000: " + primeGenerator.primes().size());
	}
}