package world;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import terrain.TileMap;

public class World {
	
	/** Class to load and store tile data */
	private final TileMap terrain;
	
	public World(Map<String, String> config, File terrainData) throws IOException {
		try {
			terrain = new TileMap(config, terrainData);
		} catch (IOException e) {
			System.err.println("Failed to load terrain.");
			throw e;
		}
	}
	
	public void draw(Graphics g, int width, int height) {
		terrain.draw(g, width, height);
	}
}
