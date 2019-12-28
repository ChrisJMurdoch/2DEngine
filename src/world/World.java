package world;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import entities.Observer;
import entities.SpriteAsset;
import terrain.TileMap;

public class World {
	
	/** Class to load and store tile data */
	private final TileMap terrain;
	
	/** Sprite data */
	private final SpriteAsset[] spriteAssets;
	
	/** Player and main observer */
	private Observer player;
	
	public World(File terrainData, File spriteData) throws IOException {
		try {
			
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
		player = new Observer(5, 5, spriteAssets[0]);
	}
	
	public void draw(Graphics g) {
		
		// Get time for animations
		long time = System.currentTimeMillis();
		
		// Draw world
		terrain.draw(g, player);
		
		// Draw sprites
		player.draw(g, player, time);
		
		// Debug
		// g.drawLine(0, 0, 1920, 1080);
		// g.drawLine(0, 1080, 1920, 0);
	}
	
	public void movePlayer(double x, double y) {
		player.move(x,y);
	}
}
