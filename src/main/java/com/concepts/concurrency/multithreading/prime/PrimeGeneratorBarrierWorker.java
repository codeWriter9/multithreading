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

	private PrimeGenerator primeGenerator;

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
		int primes_last = primes.get(primes.size() - 1);
		LOG.debug(" primes_last = " + primes_last);
		int start = primes_last + 1;
		int end = primes_last * primes_last > primeGenerator.upperBound() ? primeGenerator.upperBound()
				: primes_last * primes_last;
		LOG.debug(" end = " + end);
		int step = (int) end / 10;
		end = start + step;
		for (PrimeGeneratorWorker worker : workers) {
			worker.restart(start, end);
			new Thread(worker).start();
			start = end;
			end = end + step > primeGenerator.upperBound() ? primeGenerator.upperBound() : end + step;			
		}
		primeGenerator.lastSubmitted(end);
		LOG.info(" Merging Completed ");
	}
}