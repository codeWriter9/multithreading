package com.concepts.concurrency.multithreading.color;

/**
 * 
 * Status maintains the current color and is the messenger between the different ColorPrinter
 * This is also the object on which the lock is maintained 
 * 
 * @author Sanjay Ghosh
 *
 */
public class Status {
	
	private Color color;

	/**
	 * 
	 * 
	 * @param color
	 */
	public Status(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * 
	 * @param color
	 */
	public synchronized void set(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * 
	 * @param color
	 * @return
	 */
	public synchronized boolean isNot(Color color) {
		return this.color != color;
	}

}