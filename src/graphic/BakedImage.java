package graphic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.MainEngine;
import entities.Observer;

public class BakedImage {
	
	private BufferedImage image;
	private int x, y;
	
	public BakedImage(BufferedImage image, int x, int y) {
		this.image = image;
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g, Observer observer) {

		// Get current difference to baked image
		int changedX = observer.xOffset() - x;
		int changedY = observer.yOffset() - y;
		
		// Draw
		g.drawImage(image, -MainEngine.BORDER - changedX, -MainEngine.BORDER - changedY, null);
	}

	public void drawUncompensated(Graphics g, Observer observer) {
		// Draw
		g.drawImage(image, 0, 0, null);
	}
	
	public boolean validate(Observer observer) {
		return ! ( observer.xOffset() < x-10 || observer.xOffset() > x+10 || observer.yOffset() < y-10 || observer.yOffset() > y+10 );
	}
}
