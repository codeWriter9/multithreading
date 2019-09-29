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
	 * Parameterized constructor to state the color of the state object
	 * 
	 * @param color
	 */
	public Status(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * Sets the Color of the state object
	 * 
	 * @param color
	 */
	public synchronized void set(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * Returns True if the parameter color is the current color
	 * 
	 * @param color
	 * @return boolean
	 */
	public synchronized boolean isNot(Color color) {
		return this.color != color;
	}

}