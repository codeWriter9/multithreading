
package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

import org.slf4j.Logger;

/**
 * 
 * 
 * 
 * 
 * @author Sanjay Ghosh
 *
 */
public class PrimeGeneratorWorker implements Runnable {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	private int end;
	private int candidate;
	PrimeGenerator generator;
	List<Integer> candidates;
	boolean processed;

	/**
	 * 
	 * 
	 * 
	 * @param start
	 * @param end
	 * @param generator
	 */
	public PrimeGeneratorWorker(int start, int end, PrimeGenerator generator) {
		this.end = end;
		this.candidate = start;
		this.candidates = new ArrayList<Integer>();
		this.processed = false;
		this.generator = generator;		
	}

	/**
	 * 
	 * 
	 * @param start
	 * @param end
	 */
	public void restart(int start, int end) {
		this.end = end;
		this.candidate = start;
		this.candidates.clear();		
		this.processed = false;
	}

	/**
	 * 
	 * 
	 * 
	 */
	@Override
	public void run() {
		LOG.debug(" [ " + Thread.currentThread().getName() + " ]  | start " + candidate + " | end " + end);
		process();
		try {

			LOG.debug(" processed T [ " + Thread.currentThread().getName() + " ] ");
			generator.barrier().await();
			
		} catch (InterruptedException e) {
			LOG.error(" =======>  InterruptedException ");			
			LOG.error(e.getMessage());
			e.printStackTrace();
			return;
		} catch (BrokenBarrierException e) {
			LOG.error(" =======>  BrokenBarrierException [ " + Thread.currentThread().getName() + "] ");
			LOG.error(e.getMessage());
			e.printStackTrace();
			return;
		}

	}

	/**
	 * 
	 * 
	 * 
	 * @return boolean
	 */ 
	private boolean hasProcessed() {
		return candidate >= end;
	}

	/**
	 * 
	 * 
	 * @return boolean
	 */
	private boolean hasNotProcessed() {
		return !hasProcessed();
	}

	/**
	 * 
	 * 
	 */
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
		processed = true;
		generator.processedThreads.add(processed);
		primes = null;
	}

	/**
	 * 
	 * 
	 * @return List<Integer>
	 */
	List<Integer> candidates() {
		return candidates;
	}

	/**
	 * 
	 * @return String  
	 */
	@Override
	public String toString() {
		return "Prime Generator Worker";
	}
}
