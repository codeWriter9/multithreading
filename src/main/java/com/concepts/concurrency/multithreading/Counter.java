package com.concepts.concurrency.multithreading;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * General purpose Thread safe class that can be used for counting
 * 
 * @author Sanjay Ghosh
 *
 */
public final class Counter {
	
	private Long counterValue = Long.valueOf(0L);
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
			System.err.println(e.getMessage());
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