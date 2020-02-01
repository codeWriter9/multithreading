package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

public class PrimeGenerator {

	private static final Logger LOG = getLogger(lookup().lookupClass());

	public static final Integer NUMBER_OF_WORKERS = 10;

	final Integer upperBound;
	final CyclicBarrier barrier;
	CopyOnWriteArrayList<Integer> primes;
	Status status;
	final Lock lock = new ReentrantLock();
	List<PrimeGeneratorWorker> workers = new ArrayList<PrimeGeneratorWorker>(NUMBER_OF_WORKERS);	
	Integer lastSubmitted;
	List<Boolean> processedThreads;
	


	public PrimeGenerator(Integer upperBound, CyclicBarrier barrier) {
		this.upperBound = upperBound;
		this.barrier = barrier == null ? defaultBarrier() : barrier;
		primes = new CopyOnWriteArrayList<>();
	
		processedThreads = new ArrayList<Boolean>(0);
		kickOff();
	}

	private void kickOff() {
		primes.add(2);
		primes.add(3);
		primes.add(5);
		primes.add(7);
		LOG.debug(" primes = " + primes);
		int start = 8;
		int end = (int) (upperBound / 10) > 1000 ? 1000 : (upperBound / 10);
		LOG.debug(" end = " + end);
		int step = end;
		for (int numberOfWorkers = 0; numberOfWorkers < NUMBER_OF_WORKERS; numberOfWorkers++) {
			PrimeGeneratorWorker pgworker = new PrimeGeneratorWorker(start, end, this);
			lastSubmitted = end;
			workers.add(pgworker);
			new Thread(pgworker, Integer.toString(numberOfWorkers)).start();
			start = end;
			end = end + step;
		}
		LOG.debug(" workers = " + workers);
		lastSubmitted = end;
	}
	

	public CyclicBarrier barrier() {
		return barrier;
	}

	public Lock lock() {
		return lock;
	}

	synchronized List<Integer> primesSync() {
		synchronized (this.primes) {
			return primes;
		}
	}

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

	public CyclicBarrier defaultBarrier() {
		return new CyclicBarrier(NUMBER_OF_WORKERS, merger());
	}

	private Runnable merger() {
		return new PrimeGeneratorBarrierWorker(this);
	}
	

	/**
	 * 
	 * 
	 * @return
	 */
	public List<Integer> primes() {
		return new ArrayList<Integer>(primes);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isDone() {
		return this.upperBound <= this.lastSubmitted;
	}

	/**
	 * 
	 * 
	 * 
	 * @return
	 */
	public Integer lastSubmitted() {
		return this.lastSubmitted;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Integer upperBound() {
		return this.upperBound;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public List<PrimeGeneratorWorker> workers() {
		return workers;
	}
}