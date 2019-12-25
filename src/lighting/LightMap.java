package lighting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.MainEngine;
import graphic.BakedImage;
import terrain.Tile;
import terrain.TileMap;

public class LightMap {
	
	private BakedImage image;
	
	private TileMap map;
	
	public LightMap(TileMap map) {
		this.map = map;
	}
	
	/** Update baked image. */
	public BakedImage update(int ox, int oy) {
		
		// Create image
		BufferedImage img = new BufferedImage(MainEngine.WINDOW_WIDTH + MainEngine.BORDER*2, MainEngine.WINDOW_HEIGHT + MainEngine.BORDER*2, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		
		// Get Tile matrix
		Tile[][] map = this.map.getMap();
		
		// Draw lights
		g.setColor(new Color(255, 255, 255, 100));
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				int border = 50;
				if (map[y][x].getAsset().getCode() == 3)
					g.fillRect((x*MainEngine.UNIT) - ox - border, (y*MainEngine.UNIT) - oy - border, MainEngine.UNIT+border*2, MainEngine.UNIT+border*2);
			}
		}
		
		// Create BakedImage
		image = new BakedImage(img, ox, oy);
		return image;
	}
}
