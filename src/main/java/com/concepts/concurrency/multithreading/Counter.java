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
	 * 
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
	 * 
	 * @return
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
	 * 
	 * @return
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
	 * 
	 * @return
	 */
	public Long longValue() {
		synchronized (lock) {
			return counterValue.longValue();
		}
	}
}