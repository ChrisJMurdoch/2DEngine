package core;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import entities.Observer;
import graphic.BakedImage;
import lighting.LightMap;
import terrain.TileMap;

public class TerrainEngine extends AbstractEngine {
	
	// References
	private TileMap map;
	private LightMap lighting;
	
	// Update information
	private Observer observer;
	
	// Images
	private BakedImage one;
	private BakedImage two;
	private BakedImage three;
	private BakedImage active;
	
	public TerrainEngine(TileMap map, Observer observer) {
		super();
		
		this.map = map;
		lighting = new LightMap(map);
		this.observer = observer;
		
		active = one;
		
		start();
	}
	
	public void draw(Graphics g) {
		if (active != null)
			active.draw(g, observer);
	}

	@Override
	public void tick(double secondsElapsed) {
		if ( active == null || !active.validate(observer) ) {
			
			// Get observer position
			int ox = observer.xOffset();
			int oy = observer.yOffset();
			
			// Determine image to update
			BufferedImage compound = new BufferedImage(MainEngine.WINDOW_WIDTH + MainEngine.BORDER*2, MainEngine.WINDOW_HEIGHT + MainEngine.BORDER*2, BufferedImage.TYPE_INT_RGB);
			if (active == one) {
				map.update(ox, oy).drawUncompensated(compound.getGraphics(), observer);
				lighting.update(ox, oy).drawUncompensated(compound.getGraphics(), observer);
				two = new BakedImage(compound, ox, oy);
				Debug.DEBUG_THREE = "Terrain page: 2";
				active = two;
			} else if (active == two) {
				map.update(ox, oy).drawUncompensated(compound.getGraphics(), observer);
				lighting.update(ox, oy).drawUncompensated(compound.getGraphics(), observer);
				three = new BakedImage(compound, ox, oy);
				Debug.DEBUG_THREE = "Terrain page: 3";
				active = three;
			} else {
				map.update(ox, oy).drawUncompensated(compound.getGraphics(), observer);
				lighting.update(ox, oy).drawUncompensated(compound.getGraphics(), observer);
				one = new BakedImage(compound, ox, oy);
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
