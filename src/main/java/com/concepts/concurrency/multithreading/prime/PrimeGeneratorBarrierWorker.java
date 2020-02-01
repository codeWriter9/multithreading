package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.sort;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

/**
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class PrimeGeneratorBarrierWorker implements Runnable {
	
	private static final Logger LOG = getLogger(lookup().lookupClass());

	PrimeGenerator primeGenerator;

	/**
	 * 
	 * 
	 * @param primeGenerator
	 */
	public PrimeGeneratorBarrierWorker(PrimeGenerator primeGenerator) {
		this.primeGenerator = primeGenerator;
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void run() {
		LOG.info(" Merging Started ");
		List<Integer> primes = primeGenerator.primesSync();
		List<PrimeGeneratorWorker> workers = primeGenerator.workers();
		for (PrimeGeneratorWorker worker : workers) {
			List<Integer> candiates = worker.candidates();
			primes.addAll(candiates);
		}
		sort(primes);
		primeGenerator.primesSync(primes);
		if (primeGenerator.isDone()) {
			LOG.info(" Merging Completed ");
			return;
		}
		int start = primeGenerator.lastSubmitted();
		int end = (int) (primeGenerator.upperBound() / 10) > 1000 ? start + 1000 : start + (primeGenerator.upperBound() / 10);
		int step = end;
		for (PrimeGeneratorWorker worker : workers) {
			worker.restart(start, end);			
			new Thread(worker).start();
			start = end;
			end = end + step;			
		}
		primeGenerator.lastSubmitted = end;		
		LOG.info(" Merging Completed ");
	}
}