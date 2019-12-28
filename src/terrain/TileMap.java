package terrain;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import core.Debug;
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
		
		// Get supersample ratio
		double ssRatio = MainEngine.SSAA;
		/** Supersample tile size */
		int SS = (int)(MainEngine.UNIT * ssRatio);
		
		// Create image
		int dimension = radius * SS * 2;
		BufferedImage shadowmap = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
		Graphics g = shadowmap.getGraphics();
		
		// Add shadows
		g.setColor(new Color(0, 0, 0, 255));
		int origin = (int)( dimension/2 - (0.5*SS) );
		for ( int yDif= -radius; yDif<radius+1; yDif++) {
			for ( int xDif= -radius; xDif<radius+1; xDif++) {
				try {
					if ( map[y+yDif][x+xDif].getAsset().opaque ) {
						//g.fillRect(origin + xDif*MainEngine.UNIT, origin + yDif*MainEngine.UNIT, MainEngine.UNIT, MainEngine.UNIT);
						//g.drawLine(dimension/2, dimension/2, origin + (int)((xDif+0.5)*MainEngine.UNIT), origin + (int)((yDif+0.5)*MainEngine.UNIT));
						
						// Create points
						Point p1 = new Point( origin+( (xDif)*SS ), origin+( (yDif)*SS ) );
						Point p2 = new Point( origin+( (xDif+1)*SS ), origin+( (yDif)*SS ) );
						Point p3 = new Point( origin+( (xDif+1)*SS ), origin+( (yDif+1)*SS ) );
						Point p4 = new Point( origin+( (xDif)*SS ), origin+( (yDif+1)*SS ) );
						
						// Get gradient
						p1.setM( getAngle(p1.x, p1.y, origin, dimension) + 180 );
						p2.setM( getAngle(p2.x, p2.y, origin, dimension) + 180 );
						p3.setM( getAngle(p3.x, p3.y, origin, dimension) + 180 );
						p4.setM( getAngle(p4.x, p4.y, origin, dimension) + 180 );
						//System.out.println(p1.m);
						//System.out.println(p2.m);
						//System.out.println(p3.m);
						//System.out.println(p4.m + "\n");
						
						// Get first gradient
						Point hm = p2.m > p1.m ? p2 : p1;
						hm = p3.m > hm.m ? p3 : hm;
						hm = p4.m > hm.m ? p4 : hm; 
						
						// Get last gradient
						Point lm = p2.m < p1.m ? p2 : p1;
						lm = p3.m < lm.m ? p3 : lm;
						lm = p4.m < lm.m ? p4 : lm;
						
						// Handle boundary exception
						if (hm.m - lm.m > 180) {
							
							// Middle outer terms
							if (p1 == lm)
								p1.m += 180;
							if (p2 == lm)
								p2.m += 180;
							if (p3 == lm)
								p3.m += 180;
							if (p4 == lm)
								p4.m += 180;
							if (p1 == hm)
								p1.m -= 180;
							if (p2 == hm)
								p2.m -= 180;
							if (p3 == hm)
								p3.m -= 180;
							if (p4 == hm)
								p4.m -= 180;
							
							// Recalculate
							hm = p2.m > p1.m ? p2 : p1;
							hm = p3.m > hm.m ? p3 : hm;
							hm = p4.m > hm.m ? p4 : hm;
							
							lm = p2.m < p1.m ? p2 : p1;
							lm = p3.m < lm.m ? p3 : lm;
							lm = p4.m < lm.m ? p4 : lm;
						}
						
						// Get projections
						Point lp = lm.project(new Point(dimension/2, dimension/2), dimension);
						Point hp = hm.project(new Point(dimension/2, dimension/2), dimension);
						
						// Create polygon
						Polygon poly = new Polygon( new int[] { hm.x, hp.x, lp.x, lm.x }, new int[] { hm.y, hp.y, lp.y, lm.y }, 4);
						
						// Fill shadows
						g.fillPolygon(poly);
						
						// Fill Tiles
						g.fillRect(p1.x, p1.y, SS, SS);
					}
				} catch (ArrayIndexOutOfBoundsException e) {}
			}
		}
		
		BufferedImage bImg = new BufferedImage((int)(shadowmap.getWidth()/ssRatio), (int)(shadowmap.getHeight()/ssRatio), BufferedImage.TYPE_INT_ARGB);
		bImg.getGraphics().drawImage(shadowmap.getScaledInstance((int)(shadowmap.getWidth()/ssRatio), (int)(shadowmap.getHeight()/ssRatio), Image.SCALE_AREA_AVERAGING), 0, 0, null);
		
		return bImg;
	}
	
	private double getAngle(int x, int y, int origin, int dimension) {
		return Math.atan2( dimension/2 - y , dimension/2 - x ) * 180 / Math.PI;
	}
	
	public boolean isColliding(Point2D[] points) {
		for (Point2D i : points) {
			try {
				if (map[(int) Math.floor(i.getY())][(int) Math.floor(i.getX())].getAsset().opaque) {
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return true;
			}
		}
		return false;
	}
	
	public void changeTile(int x, int y, int code) {
		
		// Get position
		int xi = x/MainEngine.UNIT, yi = y/MainEngine.UNIT;
		
		// Change tile
		try {
			map[yi][xi].setAsset(assets[code]);
			if (assets[code].illuminationRadius == 0)
				map[yi][xi].light = null;
		} catch (ArrayIndexOutOfBoundsException e) {
			return;
		}
		
		// Update lights in radius
		int radius = MainEngine.MAX_ILLUMINATION_RADIUS;
		for ( int yDif= -radius; yDif<radius+1; yDif++) {
			for ( int xDif= -radius; xDif<radius+1; xDif++) {
				try {
					if (map[yi+yDif][xi+xDif].getAsset().illuminationRadius > 0)
						map[yi+yDif][xi+xDif].light = new Light( map[yi+yDif][xi+xDif].getAsset().illuminationRadius, getShadowmap(xi+xDif, yi+yDif, map[yi+yDif][xi+xDif].getAsset().illuminationRadius) );
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
			}
		}
		
		// Update orientations of adjacent tiles
		for ( int yDif= -1; yDif<2; yDif++) {
			for ( int xDif= -1; xDif<2; xDif++) {
				try {
					map[yi+yDif][xi+xDif].setOrientation(getOrientationCode(xi+xDif, yi+yDif));
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
			}
		}
		
		// Update baked lighting in radius
		for ( int yDif= -radius; yDif<radius+1; yDif++) {
			for ( int xDif= -radius; xDif<radius+1; xDif++) {
				try {
					map[yi+yDif][xi+xDif].update(getLightmap( xi+xDif, yi+yDif ));
				} catch (ArrayIndexOutOfBoundsException e) {
					
				}
			}
		}
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
	
	private class Point {
		
		private int x, y;
		private double m;
		
		private Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		private void setM(double m) {
			this.m = m;
		}
		
		private Point project(Point from, int toX) {
			int xDif = x - from.x;
			int yDif = y - from.y;
			int toDif = toX - from.x;
			double mult = Math.abs((double)(toDif) / (double)xDif);
			int scaledY = (int)( (double)yDif * mult );
			toX = x < from.x ? 0 : toX;
			return new Point(toX, scaledY+from.y);
		}
	}
}
