package world;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.io.File;
import java.io.IOException;

import core.MainEngine;
import entities.Observer;
import entities.SpriteAsset;
import resource.LoaderUtility;
import terrain.TileMap;

public class World {
	
	/** Class to load and store tile data */
	private final TileMap terrain;
	
	/** Sprite data */
	private final SpriteAsset[] spriteAssets;
	
	private final Image highlight;
	
	/** Player and main observer */
	private Observer player;
	
	public World(File terrainData, File spriteData, File misc) throws IOException {
		try {
			
			// Load misc
			highlight = LoaderUtility.loadImage(new File(misc.getPath() + "//16x16Highlight.png")).getScaledInstance(MainEngine.UNIT, MainEngine.UNIT, Image.SCALE_DEFAULT);
			
			// Load terrain
			terrain = new TileMap(terrainData);
			
			// Load sprites
			long baseTime = System.currentTimeMillis();
			File[] spriteFiles = spriteData.listFiles();
			spriteAssets = new SpriteAsset[spriteFiles.length];
			for (int i=0; i<spriteAssets.length; i++) {
				spriteAssets[i] = new SpriteAsset(spriteFiles[i], baseTime);
			}
		} catch (IOException e) {
			System.err.println("Failed to load terrain.");
			throw e;
		}
		player = new Observer(8, 14, spriteAssets[0]);
	}
	
	public void draw(Graphics g) {
		
		// Get time for animations
		long time = System.currentTimeMillis();
		
		// Draw world
		terrain.draw(g, player);
		
		// Draw sprites
		player.draw(g, player, time);
		
		// Draw cursor
		int x = MouseInfo.getPointerInfo().getLocation().x;
		int y = MouseInfo.getPointerInfo().getLocation().y;
		x += player.xOffset();
		y += player.yOffset();
		x -= x % MainEngine.UNIT;
		y -= y % MainEngine.UNIT;
		x -= player.xOffset();
		y -= player.yOffset();
		g.drawImage(highlight, x, y, null);
		
		// Debug
		// g.drawLine(0, 0, 1920, 1080);
		// g.drawLine(0, 1080, 1920, 0);
	}
	
	public void movePlayer(double x, double y) {
		player.move(x,y, terrain);
	}
	
	public void changeTile(int x, int y, int code) {
		terrain.changeTile(x+player.xOffset(), y+player.yOffset(), code);
	}
}
