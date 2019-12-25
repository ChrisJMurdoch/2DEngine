package world;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import core.MainEngine;
import core.TerrainEngine;
import entities.Observer;
import entities.Sprite;
import lighting.LightMap;
import terrain.TileMap;

public class World {
	
	/** Class to load and store tile data */
	private final TileMap terrain;
	
	/** Player and main observer */
	private Observer player;
	
	/** Engine on second thread for rendering expensive baked images */
	private TerrainEngine terrainEngine;
	
	public World(File terrainData) throws IOException {
		try {
			terrain = new TileMap(terrainData);
		} catch (IOException e) {
			System.err.println("Failed to load terrain.");
			throw e;
		}
		player = new Observer(8, 8, 1, 1);

		// Create updater
		terrainEngine = new TerrainEngine(terrain, player);
	}
	
	public void draw(Graphics g) {
		
		// Draw
		terrainEngine.draw(g);
		
		player.draw(g, player);
	}
	
	public void movePlayer(double x, double y) {
		player.move(x,y);
	}
}
