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
		LOG.info(primeGenerator.primes() + "");
	}

}