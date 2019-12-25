package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/** Static class to ease variable watches on-screen */
public class Debug {
	
	public static String DEBUG_ONE = "Default";
	public static String DEBUG_TWO = "Default";
	public static String DEBUG_THREE = "Default";
	
	private static int target = 120;
	private static boolean upper = false;
	private static BufferedImage bar = new BufferedImage(target, 5, BufferedImage.TYPE_INT_RGB);
	
	public static void draw(Graphics g, int x, int y) {
		
		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 96, 40);
		
		// Display data 
		g.setColor(Color.WHITE);
		g.drawString(DEBUG_ONE, x+2, y+12); 
		g.drawString(DEBUG_TWO, x+2, y+24);
		g.drawString(DEBUG_THREE, x+2, y+36);
		
		// Get time
		Graphics b = bar.getGraphics();
		long nano = System.nanoTime();
		double seconds = (double)nano / 1000000000;
		
		// Modulo to 0-1 seconds
		double capped = seconds % 1;
		
		// Multiply by target
		int scaled = (int)(capped * target);
		
		// Check reset
		if (upper && capped < 0.5) {
			upper = false;
			b.setColor(Color.BLACK);
			b.fillRect(0, 0, target, 5);
		} else if (capped > 0.5) {
			upper = true;
		}
		b.setColor(Color.WHITE);
		b.drawRect(scaled, 0, 0, 5);
		g.drawImage(bar, 105, 5, null);
	}
}
