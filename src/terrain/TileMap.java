package terrain;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import core.MainEngine;
import entities.Observer;
import graphic.BakedImage;
import resource.LoaderUtility;

public class TileMap {
	
	/** Tile asset data */
	private TileAsset[] assets;
	/** Tile matrix */
	private Tile [][] map;
	/** World image */
	private BakedImage image;
	
	public TileMap(File terrainData) throws IOException {
		
		// Load assets
		File data = new File(terrainData.getPath() + "//TileData");
		File[] tileFiles = data.listFiles();
		assets = new TileAsset[tileFiles.length];
		for (int i=0; i < tileFiles.length; i++) {
			
			// Get images
			Image[] images = LoaderUtility.loadImageArrayOrdered(new File(tileFiles[i].getPath() + "//Images"));
			
			// Get merges
			String[] merges = LoaderUtility.loadTextMap(new File(tileFiles[i].getPath() + "//Data.txt")).get("merges").split(",");
			int[] intMerges = new int[merges.length];
			for (int j=0; j < merges.length; j++) {
				intMerges[j] = Integer.parseInt(merges[j]);
			}
			
			// Create asset
			assets[i] = new TileAsset(i, images, intMerges);
		}
		
		// Load tiles
		File mapFile = new File(terrainData.getPath() + "//Map.txt");
		String[] lines = LoaderUtility.loadTextFile(mapFile);
		map = new Tile[lines.length][];
		for (int i=0; i<lines.length; i++) {
			String[] words = lines[i].split(",");
			map[i] = new Tile[words.length];
			for (int j=0; j<words.length; j++) {
				map[i][j] = new Tile (assets[Integer.parseInt(words[j])]);
			}
		}
		
		// Calculate orientations
		for (int i=0; i<map.length; i++) {
			for (int j=0; j<map[0].length; j++) {
				map[i][j].setOrientation(this.getOrientationCode(j, i));
			}
		}
	}
	
	/** Update baked image. */
	public BakedImage update(int ox, int oy) {
		
		// Create image
		BufferedImage img = new BufferedImage(MainEngine.WINDOW_WIDTH + MainEngine.BORDER*2, MainEngine.WINDOW_HEIGHT + MainEngine.BORDER*2, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		
		// Draw tiles
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				map[y][x].draw( g, (x*MainEngine.UNIT) - ox, (y*MainEngine.UNIT) - oy );
			}
		}
		
		// Create BakedImage
		image = new BakedImage(img, ox, oy);
		return image;
	}
	
	/** For use by lighting library */
	public Tile[][] getMap() {
		return map;
	}
	
	/** Calculate orientation code based on surrounding tiles */
	private int getOrientationCode(int x, int y) {
		
		// Get surrounding merges
		Tile tile = map[y][x];
		boolean north = false, east = false, south = false, west = false;
		try { north = tile.getAsset().merges(map[y-1][x].getAsset()); } catch (Exception e) {};
		try { east = tile.getAsset().merges(map[y][x+1].getAsset()); } catch (Exception e) {};
		try { south = tile.getAsset().merges(map[y+1][x].getAsset()); } catch (Exception e) {};
		try { west = tile.getAsset().merges(map[y][x-1].getAsset()); } catch (Exception e) {};
		
		// Optimise for switch-case
		int count = 0;
		if (north)
			count++;
		if (east)
			count++;
		if (south)
			count++;
		if (west)
			count++;
		
		// Determine orientation code
		switch (count) {
		
		case 0:
			return 0;
			
		case 1:
			if (north)
				return 1;
			if (east)
				return 2;
			if (south)
				return 3;
			if (west)
				return 4;
			
		case 2:
			if (north) {
				if (east)
					return 5;
				if (south)
					return 9;
				if (west)
					return 8;
			}
			if (south) {
				if (east)
					return 6;
				if (west)
					return 7;
			}
			return 10;
			
		case 3:
			if (!north)
				return 11;
			if (!east)
				return 12;
			if (!south)
				return 13;
			if (!west)
				return 14;
			
		case 4:
			return 15;
			
		default:
			return 0;
		}
	}
}
