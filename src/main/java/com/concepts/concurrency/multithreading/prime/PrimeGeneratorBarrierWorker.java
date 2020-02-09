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
		int start = primeGenerator.start();
		int end = primeGenerator.end();
		LOG.debug(" end = " + end);
		int step = (int) end / 10;
		end = start + step;
		int numberOfWorkers = 0;
		for (PrimeGeneratorWorker worker : workers) {
			worker.restart(start, end);
			primeGenerator.createThreadAndStart(worker, numberOfWorkers);
			start = end;
			end = primeGenerator.stepEnd(end, step);;
			numberOfWorkers++;
		}
		primeGenerator.lastSubmitted(end);
		LOG.info(" Merging Completed ");
	}
}