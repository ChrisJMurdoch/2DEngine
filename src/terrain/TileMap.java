package terrain;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
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
	
	/** Tile assets */
	private TileAsset[] assets;
	/** Tile matrix */
	private int [][] map;
	/** Baked terrain image */
	private BufferedImage image;
	
	public TileMap(Map<String, String> config, File terrainData) throws IOException {
		
		// Get config data
		DENSITY = Integer.parseInt(config.get("horizontal_tile_density"));
		
		// Load assets
		File data = new File(terrainData.getPath() + "//TileData");
		File[] tileFiles = data.listFiles();
		assets = new TileAsset[tileFiles.length];
		for (int i=0; i < tileFiles.length; i++) {
			
			// Get images
			Image[] images = LoaderUtility.loadImageArray(new File(tileFiles[i].getPath() + "//Images"));
			
			// Get merges
			String[] merges = LoaderUtility.loadTextMap(new File(tileFiles[i].getPath() + "//Data.txt")).get("merges").split(",");
			int[] intMerges = new int[merges.length];
			for (int j=0; j < merges.length; j++) {
				intMerges[j] = Integer.parseInt(merges[j]);
			}
			
			// Create asset
			assets[i] = new TileAsset(images, intMerges);
		}
		
		// Load tiles
		File mapFile = new File(terrainData.getPath() + "//Map.txt");
		String[] lines = LoaderUtility.loadTextFile(mapFile);
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
		
		// Validate assets
		if (assets[0].getScale() != tileDim) {
			for (TileAsset i : assets) {
				i.scale(tileDim);
			}
		}
		
		// Create new image
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		
		// Draw background
		g.setColor(Color.RED);
		g.fillRect(0, 0, width, height);
		
		// Draw tiles
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				int code = map[y][x];
				g.drawImage(assets[code].scaled[0], x*tileDim, y*tileDim, null);
			}
		}
	}
	
	private class TileAsset {
		/** All visual representations of material */
		private final Image[] images;
		/** All visual representations of material, scaled */
		private Image[] scaled;
		/** The codes of tiles this tile merges with */
		private int[] merges;
		private TileAsset(Image[] images, int[] merges) {
			this.images = images;
			this.merges = merges;
			// Shallow copy
			scaled = new Image[images.length];
			for (int i=0; i < images.length; i++) {
				scaled[i] = images[i];
			}
		}
		private int getScale() {
			return scaled[0].getWidth(null);
		}
		private void scale(int dimension) {
			for (int i=0; i< images.length; i++) {
				scaled[i] = images[i].getScaledInstance(dimension, dimension, Image.SCALE_DEFAULT);
			}
		}
	}
	
	private class Tile {
		private int tileCode;
		private int orientationCode;
		private Tile(int tileCode, int orientationCode) {
			this.tileCode = tileCode;
			this.orientationCode = orientationCode;
		}
	}
}
