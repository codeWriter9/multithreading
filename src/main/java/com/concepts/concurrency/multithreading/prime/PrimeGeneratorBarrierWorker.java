package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Collections.sort;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.slf4j.Logger;

public class PrimeGeneratorBarrierWorker implements Runnable {
	
	private static final Logger LOG = getLogger(lookup().lookupClass());

	PrimeGenerator primeGenerator;

	public PrimeGeneratorBarrierWorker(PrimeGenerator primeGenerator) {
		this.primeGenerator = primeGenerator;
	}

	@Override
	public void run() {
		LOG.info(" Merging Started ");
		List<Integer> primes = primeGenerator.primesSync();
		List<PrimeGeneratorWorker> workers = primeGenerator.worker;
		for (PrimeGeneratorWorker worker : workers) {
			List<Integer> candiates = worker.candidates();
			primes.addAll(candiates);
		}
		sort(primes);
		int start = primeGenerator.lastSubmitted;
		int end = (int) (primeGenerator.upperBound / 10) > 1000 ? 1000 : (primeGenerator.upperBound / 10);
		int step = end;
		for (PrimeGeneratorWorker worker : workers) {
			worker.restart(start, end);		
			primeGenerator.worker.add(worker);
			new Thread(worker).start();
			start = end;
			end = end + step;			
		}
		primeGenerator.lastSubmitted = end;
		LOG.info(" Merging Completed ");
	}
}