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
					map[y][x].light = new Light( map[y][x].getAsset().illuminationRadius, getShadowmap(x, y, map[y][x].getAsset().illuminationRadius) );
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
	
	/** Get shadowmap for tile of given indexes */
	private BufferedImage getShadowmap(int x, int y, int radius) {
		
		// Create image
		int dimension = radius * MainEngine.UNIT * 2;
		BufferedImage shadowmap = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
		Graphics g = shadowmap.getGraphics();
		
		// Add shadows
		g.setColor(new Color(0, 0, 0, 255));
		int origin = (int)( dimension/2 - (0.5*MainEngine.UNIT) );
		for ( int yDif= -radius; yDif<radius+1; yDif++) {
			for ( int xDif= -radius; xDif<radius+1; xDif++) {
				try {
					if ( map[y+yDif][x+xDif].getAsset().opaque ) {
						//g.fillRect(origin + xDif*MainEngine.UNIT, origin + yDif*MainEngine.UNIT, MainEngine.UNIT, MainEngine.UNIT);
						//g.drawLine(dimension/2, dimension/2, origin + (int)((xDif+0.5)*MainEngine.UNIT), origin + (int)((yDif+0.5)*MainEngine.UNIT));
						
						// Get gradient
						double m1 = getAngle(yDif, xDif, origin, dimension) + 180;
						System.out.println(m1);
						double m2 = getAngle(yDif, xDif+1, origin, dimension) + 180;
						System.out.println(m2);
						double m3 = getAngle(yDif+1, xDif+1, origin, dimension) + 180;
						System.out.println(m3);
						double m4 = getAngle(yDif+1, xDif, origin, dimension) + 180;
						System.out.println(m4 + "\n");
						
						// Get first gradient
						double hm = m2 > m1 ? m2 : m1;
						hm = m3 > hm ? m3 : hm;
						hm = m4 > hm ? m4 : hm;
						
						// Get last gradient
						double lm = m2 < m1 ? m2 : m1;
						lm = m3 < lm ? m3 : lm;
						lm = m4 < lm ? m4 : lm;
						
						// Handle boundary exception
						if (hm - lm > 180) {
							
							// Middle outer terms
							if (m1 == lm)
								m1 += 180;
							if (m2 == lm)
								m2 += 180;
							if (m3 == lm)
								m3 += 180;
							if (m4 == lm)
								m4 += 180;
							
							if (m1 == hm)
								m1 -= 180;
							if (m2 == hm)
								m2 -= 180;
							if (m3 == hm)
								m3 -= 180;
							if (m4 == hm)
								m4 -= 180;
							
							// Recalculate
							hm = m2 > m1 ? m2 : m1;
							hm = m3 > hm ? m3 : hm;
							hm = m4 > hm ? m4 : hm;
							
							lm = m2 < m1 ? m2 : m1;
							lm = m3 < lm ? m3 : lm;
							lm = m4 < lm ? m4 : lm;
						}
						
						// Draw
						if (m1 == hm || m1 == lm)
							g.drawLine(dimension/2, dimension/2, origin + (int)((xDif+0)*MainEngine.UNIT), origin + (int)((yDif+0)*MainEngine.UNIT));
						if (m2 == hm || m2 == lm)
							g.drawLine(dimension/2, dimension/2, origin + (int)((xDif+1)*MainEngine.UNIT), origin + (int)((yDif+0)*MainEngine.UNIT));
						if (m3 == hm || m3 == lm)
							g.drawLine(dimension/2, dimension/2, origin + (int)((xDif+1)*MainEngine.UNIT), origin + (int)((yDif+1)*MainEngine.UNIT));
						if (m4 == hm || m4 == lm)
							g.drawLine(dimension/2, dimension/2, origin + (int)((xDif+0)*MainEngine.UNIT), origin + (int)((yDif+1)*MainEngine.UNIT));
						
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		
		return shadowmap;
	}
	
	private double getAngle(int x, int y, int origin, int dimension) {
		return Math.atan2( dimension/2 - ( origin+(y*MainEngine.UNIT) ) , dimension/2 - ( origin+(x*MainEngine.UNIT) ) ) * 180 / Math.PI;
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
