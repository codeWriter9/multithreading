package com.concepts.concurrency.multithreading;

import static java.lang.Long.valueOf;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

/**
 * 
 * General purpose Thread safe class that can be used for counting
 * 
 * @author Sanjay Ghosh
 *
 */
public final class Counter {
	
	private static final Logger LOG = getLogger(lookup().lookupClass());
	
	private Long counterValue = valueOf(0L);
	private Lock lock;
	
	/**
	 * 
	 * 
	 */
	public Counter() {
		lock = new ReentrantLock();
	}
	
	/**
	 * 
	 * waits on lock
	 * 
	 */
	public void waitOnLock() {
		try {
			lock.wait();
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * Increments and returns a Integer value
	 * 
	 * @return Integer
	 */
	public Integer incrementAndIntValue() {
		synchronized (lock) {
			counterValue++;
			lock.notifyAll();
			return counterValue.intValue();
		}
	}
	
	/**
	 * 
	 * Increments and returns a Long value
	 * 
	 * @return Long
	 */
	public Long incrementAndlongValue() {
		synchronized (lock) {
			counterValue++;
			lock.notifyAll();
			return counterValue.longValue();
		}
	}
	
	 
	
	/**
	 * 
	 * Return the Integer value of the counter
	 * 
	 * @return 
	 */
	public Integer intValue() {
		synchronized (lock) {
			return counterValue.intValue();
		}
	}
	
	/**
	 * 
	 * Return the Long value of the counter
	 * 
	 * @return long value
	 */
	public Long longValue() {
		synchronized (lock) {
			return counterValue.longValue();
		}
	}
}