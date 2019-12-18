package core;

import java.awt.Color;
import java.awt.Graphics;

/** Static class to ease variable watches on-screen */
public class Debug {
	
	public static String DEBUG_ONE = "Default";
	public static String DEBUG_TWO = "Default";
	public static String DEBUG_THREE = "Default";
	
	public static void draw(Graphics g, int x, int y) {
		
		// Draw background
		g.setColor(Color.BLACK);
		g.fillRect(x, y, 100, 38);
		
		// Dsiplay data
		g.setColor(Color.WHITE);
		g.drawString(DEBUG_ONE, x+2, y+12);
		g.drawString(DEBUG_TWO, x+2, y+24);
		g.drawString(DEBUG_THREE, x+2, y+36);
	}
}
