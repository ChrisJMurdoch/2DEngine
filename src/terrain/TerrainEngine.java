package terrain;

import java.awt.image.BufferedImage;

import core.AbstractEngine;
import core.Debug;
import core.MainEngine;
import entities.Observer;

public class TerrainEngine extends AbstractEngine {
	
	// References
	private TileMap map;
	
	// Update information
	private Observer observer;
	private boolean update;
	
	// Images
	private BufferedImage one;
	private BufferedImage two;
	private BufferedImage three;
	private BufferedImage active;
	
	protected TerrainEngine(TileMap map) {
		super();
		
		this.map = map;
		update = false;
		one = new BufferedImage(MainEngine.WINDOW_WIDTH + MainEngine.BORDER*2, MainEngine.WINDOW_HEIGHT + MainEngine.BORDER*2, BufferedImage.TYPE_INT_RGB);
		two = new BufferedImage(MainEngine.WINDOW_WIDTH + MainEngine.BORDER*2, MainEngine.WINDOW_HEIGHT + MainEngine.BORDER*2, BufferedImage.TYPE_INT_RGB);
		three = new BufferedImage(MainEngine.WINDOW_WIDTH + MainEngine.BORDER*2, MainEngine.WINDOW_HEIGHT + MainEngine.BORDER*2, BufferedImage.TYPE_INT_RGB);
		active = one;
		
		start();
	}
	
	protected void queueUpdate(Observer observer) {
		this.observer = observer;
		update = true;
	}
	
	protected BufferedImage getImage() {
		return active;
	}

	@Override
	public void tick(double secondsElapsed) {
		if (update) {
			update = false;
			
			// Determine image to update
			if (active == one) {
				map.update(two.getGraphics(), observer);
				Debug.DEBUG_THREE = "Terrain page: 2";
				active = two;
			} else if (active == two) {
				map.update(three.getGraphics(), observer);
				Debug.DEBUG_THREE = "Terrain page: 3";
				active = three;
			} else {
				map.update(one.getGraphics(), observer);
				Debug.DEBUG_THREE = "Terrain page: 1";
				active = one;
			}
		} else {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Update display
		Debug.DEBUG_TWO = "Baked CPS: " + getFrameRate();
		
	}
}
