package com.concepts.concurrency.multithreading.color;

/**
 * 
 * Printer class which 
 * 
 * @author Sanjay Ghosh
 *
 */
public class ColorPrinter {
	
	private Color color;
	
	/**
	 * 
	 * Primary constructor which initializes the color
	 * 
	 * @param color
	 */
	public ColorPrinter(Color color) {
		this.color = color;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public static ColorPrinter redPrinter() {
		return new ColorPrinter(Color.RED);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public static ColorPrinter greenPrinter() {
		return new ColorPrinter(Color.GREEN);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public static ColorPrinter bluePrinter() {
		return new ColorPrinter(Color.BLUE);
	}
	
	/**
	 * 
	 * Static method to tell which color would come next
	 * 
	 * @return
	 */
	public static Color nextColor(ColorPrinter printer) {
		if (Color.RED.equals(printer.color))
			return Color.GREEN;
		else if (Color.GREEN.equals(printer.color))
			return Color.BLUE;
		else if (Color.BLUE.equals(printer.color))
			return Color.RED;
		return null;
	}

	/**
	 * 
	 * Returns the color of the instance
	 * 
	 * @return Color
	 */
	public Color color() {
		return this.color;
	}

}
