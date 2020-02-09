package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

/**
 * 
 * We start with the basic list of [2, 3, 5, 7] as the first prime numbers and then build on it
 * To keep things simple we always use 10 threads to divide the work
 * each of the thread will start checking from <code>start</code> to <code>end</code>
 * We calculate <code>end</code> by
 *       end : = upperBound <= 10 ? 10 : next step
 *       end : = upperBound > 10 && upperBound < prime[last] ? prime[last] * prime[last] : next step
 *       end : =   
 * 
 * @author Sanjay Ghosh
 *
 */
public class PrimeGenerator {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	public static final Integer NUMBER_OF_WORKERS = 10;

	private final Integer upperBound;
	private final CyclicBarrier barrier;
	private CopyOnWriteArrayList<Integer> primes;
	private final Lock lock;
	private List<PrimeGeneratorWorker> workers;
	private Integer lastSubmitted;

	/**
	 * 
	 * 
	 * 
	 * @param upperBound
	 * @param barrier
	 */
	public PrimeGenerator(Integer upperBound, CyclicBarrier barrier) {
		this.upperBound = upperBound;
		this.barrier = barrier == null ? defaultBarrier() : barrier;
		primes = new CopyOnWriteArrayList<>();
		lock = new ReentrantLock();
		workers = new ArrayList<PrimeGeneratorWorker>(NUMBER_OF_WORKERS);
		kickOff();
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 */
	private void kickOff() {
		primes.add(2);
		primes.add(3);
		primes.add(5);
		primes.add(7);
		LOG.debug(" primes = " + primes);
		if (upperBound <= 10) {
			return;
		}
		int start = 8;
		int primes_last = primes.get(primes.size() - 1);
		LOG.debug(" primes_last = " + primes_last);
		int end = primes_last * primes_last > upperBound ? upperBound : primes_last * primes_last;
		LOG.debug(" end = " + end);
		int step = (int) end / 10;
		end = start + step; 
		for (int numberOfWorkers = 0; numberOfWorkers < NUMBER_OF_WORKERS; numberOfWorkers++) {
			PrimeGeneratorWorker pgworker = new PrimeGeneratorWorker(start, end, this);
			lastSubmitted = end;
			workers.add(pgworker);
			new Thread(pgworker, Integer.toString(numberOfWorkers)).start();
			start = end;
			end = end + step > upperBound() ? upperBound() : end + step;
		}		
		lastSubmitted = end;
	}

	/**
	 * 
	 * 
	 * 
	 * @return CyclicBarrier
	 */
	public CyclicBarrier barrier() {
		return barrier;
	}

	/**
	 * 
	 * 
	 * 
	 * @return lock
	 */
	public Lock lock() {
		return lock;
	}

	/**
	 * 
	 * 
	 * 
	 * @return List<Integer>
	 */
	synchronized List<Integer> primesSync() {
		synchronized (this.primes) {
			return primes;
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @param primes
	 * @return boolean
	 */
	synchronized boolean primesSync(List<Integer> primes) {
		synchronized (this.primes) {
			try {
				this.primes.addAll(primes);
				this.primes = new CopyOnWriteArrayList<Integer>(new LinkedHashSet<Integer>(primes));
				return true;
			} catch (Exception e) {
				return false;
			}
		}
	}

	/**
	 * 
	 * 
	 * 
	 * @return CyclicBarrier
	 */
	public CyclicBarrier defaultBarrier() {
		return new CyclicBarrier(NUMBER_OF_WORKERS, merger());
	}

	/**
	 * 
	 * 
	 * @return Runnable
	 */
	private Runnable merger() {
		return new PrimeGeneratorBarrierWorker(this);
	}

	/**
	 * 
	 * 
	 * @return List<Integer>
	 */
	public List<Integer> primes() {
		return new ArrayList<Integer>(primes);
	}

	/**
	 * 
	 * 
	 * @return boolean
	 */
	public boolean isDone() {
		return this.upperBound <= this.lastSubmitted;
	}

	/**
	 * 
	 * 
	 * 
	 * @return Integer
	 */
	public Integer lastSubmitted() {
		return this.lastSubmitted;
	}

	/**
	 * 
	 * 
	 * 
	 * @return Integer
	 */
	public void lastSubmitted(int end) {
		this.lastSubmitted = end;
	}

	/**
	 * 
	 * 
	 * @return Integer
	 */
	public Integer upperBound() {
		return this.upperBound;
	}

	/**
	 * 
	 * 
	 * @return List<PrimeGeneratorWorker>
	 */
	public List<PrimeGeneratorWorker> workers() {
		return workers;
	}
}