package com.concepts.concurrency.multithreading.prime;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.concurrent.Executors.newFixedThreadPool;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
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
	List<PrimeGeneratorWorker> worker = new ArrayList<PrimeGeneratorWorker>(NUMBER_OF_WORKERS);
	ExecutorService service;
	Integer lastSubmitted;
	CountDownLatch tracker;

	public PrimeGenerator(Integer upperBound, CyclicBarrier barrier) {
		this.upperBound = upperBound;
		this.barrier = barrier == null ? defaultBarrier() : barrier;
		primes = new CopyOnWriteArrayList<>();
		service = newFixedThreadPool(NUMBER_OF_WORKERS);
		kickOff();
	}

	private void kickOff() {
		primes.add(2);
		primes.add(3);
		primes.add(5);
		primes.add(7);
		LOG.info(" primes = " + primes);
		int start = 8;
		int end = (int) (upperBound / 10) >	 1000 ? 1000 : (upperBound / 10);
		System.out.println(" end = " + end);
		int step = end;
		tracker = new CountDownLatch(NUMBER_OF_WORKERS);
		for (int workers = 0; workers < NUMBER_OF_WORKERS; workers++) {			
			PrimeGeneratorWorker pgworker = new PrimeGeneratorWorker(start, end, this);
			lastSubmitted = end;
			worker.add(pgworker);
			new Thread(pgworker, Integer.toString(workers)).start();
			start = end;
			end = end + step;
		}
		LOG.info(" workers = " + worker);
		lastSubmitted = end;
	}

	public boolean isDone() throws InterruptedException {
		LOG.info(" tracker " + tracker.getCount());
		return tracker.await(10 * 1000,TimeUnit.MILLISECONDS);
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

	public CyclicBarrier defaultBarrier() {
		return new CyclicBarrier(NUMBER_OF_WORKERS, merger());
	}

	private Runnable merger() {
		return new PrimeGeneratorBarrierWorker(this);
	}

	/**
	 * 
	 * Safely wait for your turn
	 * 
	 */
	private void safelyWait() {
		try {
			status.wait();
		} catch (InterruptedException e) {
			LOG.error(" Error message from safelyWait " + e.getMessage());
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public List<Integer> primes() {
		return new ArrayList<Integer>(primes);
	}

}
