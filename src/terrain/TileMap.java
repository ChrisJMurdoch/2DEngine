package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import resource.LoaderUtility;

public class TileMap implements Terrain {
	
	/** Tiles per width */
	private final int DENSITY;
	/** Tile pixel dimension */
	private int tileDim;
	/** Tile matrix */
	private int[][] map;
	/** Baked terrain image */
	private BufferedImage image;
	
	public TileMap(Map<String, String> config, File terrainData) throws IOException {
		
		// Get config data
		DENSITY = Integer.parseInt(config.get("horizontal_tile_density"));
		
		// Load tiles
		String[] lines = LoaderUtility.loadTextFile(terrainData);
		map = new int[lines.length][];
		for (int i=0; i<lines.length; i++) {
			String[] words = lines[i].split(",");
			map[i] = new int[words.length];
			for (int j=0; j<words.length; j++) {
				map[i][j] = Integer.parseInt(words[j]);
			}
		}
	}
	
	@Override
	public void draw(Graphics g, int width, int height) {
		
		// Check image validity
		if ( image == null || width != image.getWidth() || width != image.getWidth() ) {
			tileDim = width / DENSITY;
			update(width, height);
		}
		
		// Draw
		g.drawImage(image, 0, 0, null);
	}
	
	/** Repaint baked image */
	private void update(int width, int height) {
		
		// Create new image
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		// Draw background
		g.setColor(Color.RED);
		g.fillRect(0, 0, width, height);
		
		// Draw tiles
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				int c = (map[y][x] * 20) % 255;
				g.setColor(new Color(c,c,c));
				g.fillRect(x*tileDim, y*tileDim, tileDim, tileDim);
			}
		}
	}
}
