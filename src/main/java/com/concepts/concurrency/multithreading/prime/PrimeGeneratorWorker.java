
package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import org.slf4j.Logger;

public class PrimeGeneratorWorker implements Runnable {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	private int end;
	private int candidate;
	PrimeGenerator generator;
	List<Integer> candidates;
	boolean processed;

	public PrimeGeneratorWorker(int start, int end, PrimeGenerator generator) {
		this.end = end;
		this.candidate = start;
		this.candidates = new ArrayList<Integer>();
		this.processed = false;
		this.generator = generator;
		// LOG.info(" created process for start = " + start + " | end = " + end);
	}

	public void restart(int start, int end) {
		this.end = end;
		this.candidate = start;
		this.candidates.clear();
		;
		this.processed = false;
	}

	@Override
	public void run() {
		process();
		LOG.info(" counting down T [ " + Thread.currentThread().getName() + " ] ");
		generator.tracker.countDown();
		try {
			while (!generator.isDone()) {
				LOG.info(" processed T [ " + Thread.currentThread().getName() + " ] ");
				generator.barrier().await();
			}
		} catch (InterruptedException e) {
			System.err.println(" =======>  InterruptedException ");
			System.err.println(e.getMessage());
			return;
		} catch (BrokenBarrierException e) {
			System.err.println(" =======>  BrokenBarrierException ");
			System.err.println(e.getMessage());
			return;
		}

	}

	private boolean hasProcessed() {
		return candidate >= end;
	}

	private boolean hasNotProcessed() {
		return !hasProcessed();
	}

	private void process() {
		generator.lock().lock();
		List<Integer> primes = generator.primesSync();
		generator.lock().unlock();
		while (hasNotProcessed()) {
			boolean isPrime = true;
			for (Integer prime : primes) {
				if (prime < candidate && candidate % prime == 0) {
					isPrime = false;
					break;
				}
			}
			if (isPrime) {
				candidates.add(candidate);
			}

			candidate++;
		}
		LOG.info(" candidates = " + candidates);
		processed = true;
		primes = null;
	}

	List<Integer> candidates() {
		return candidates;
	}

	@Override
	public String toString() {
		return " worker ";
	}
}
