package terrain;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import core.MainEngine;
import entities.Observer;
import resource.LoaderUtility;

public class TileMap {
	
	/** Tile asset data */
	private TileAsset[] assets;
	/** Tile matrix */
	private Tile [][] map;
	
	public TileMap(File terrainData) throws IOException {
		
		// Load assets
		File data = new File(terrainData.getPath() + "//TileData");
		File[] tileFiles = data.listFiles();
		assets = new TileAsset[tileFiles.length];
		for (int i=0; i < tileFiles.length; i++) {
			
			// Get images
			Image[] images = LoaderUtility.loadImageArrayOrdered(new File(tileFiles[i].getPath() + "//Images"));
			
			// Create asset
			assets[i] = new TileAsset(i, images, LoaderUtility.loadTextMap(new File(tileFiles[i].getPath() + "//Data.txt")));
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
		
		// Pre-render tile lighting
		update();
	}
	
	public void draw(Graphics g, Observer observer) {
		// Draw tiles
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				map[y][x].draw( g, (x*MainEngine.UNIT) - observer.xOffset(), (y*MainEngine.UNIT) - observer.yOffset() );
			}
		}
	}
	
	/** Calculate and bake-in tile lighting */
	public void update() {
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				if (map[y][x].getAsset().illuminationRadius > 0) {
					map[y][x].light = new Light( map[y][x].getAsset().illuminationRadius );
				}
			}
		}
		for (int y=0; y<map.length; y++) {
			for (int x=0; x<map[0].length; x++) {
				map[y][x].update(getLightmap(x, y));
			}
		}
	}
	
	/** Get lightmap for tile of given indexes */
	private BufferedImage getLightmap(int x, int y) {
		
		// Create dark image
		BufferedImage lightmap = new BufferedImage( MainEngine.UNIT, MainEngine.UNIT, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = (Graphics2D)lightmap.getGraphics();
		g.setColor( new Color( 0, 0, 0, 255 - MainEngine.GLOBAL_ALPHA ));
		g.fillRect( 0, 0, MainEngine.UNIT, MainEngine.UNIT );
		
		// Subtract lights
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
		// For surrounding tiles
		int max = MainEngine.MAX_ILLUMINATION_RADIUS;
		for ( int yDif= -max; yDif<max+1; yDif++) {
			for ( int xDif= -max; xDif<max+1; xDif++) {
				try {
					if ( map[y+yDif][x+xDif].light != null ) {
						map[y+yDif][x+xDif].light.draw(g, (int)((xDif+0.5)*MainEngine.UNIT), (int)((yDif+0.5)*MainEngine.UNIT));
						//g.drawRect(10, 10, 90, 90);
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		
		return lightmap;
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
