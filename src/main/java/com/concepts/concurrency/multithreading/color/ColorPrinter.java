package com.concepts.concurrency.multithreading.color;

/**
 * 
 * Printer class which prints the current color
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
	 * static method to create a RED printer
	 * as recommended by Bloch
	 * 
	 * @return ColorPrinter
	 */
	public static ColorPrinter redPrinter() {
		return new ColorPrinter(Color.RED);
	}
	
	/**
	 * 
	 * static method to create a GREEN printer
	 * as recommended by Bloch
	 * 
	 * @return ColorPrinter
	 */
	public static ColorPrinter greenPrinter() {
		return new ColorPrinter(Color.GREEN);
	}
	
	/**
	 * 
	 * static method to create a BLUE printer
	 * as recommended by Bloch
	 * 
	 * @return ColorPrinter
	 */
	public static ColorPrinter bluePrinter() {
		return new ColorPrinter(Color.BLUE);
	}
	
	/**
	 * 
	 * Static method to tell which color would come next
	 * 
	 * @return Color pattern followed is (R)ED (G)REEN (B)LUE
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
