package world;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import core.Engine;
import entities.Observer;
import entities.Sprite;
import terrain.TileMap;

public class World {
	
	/** Class to load and store tile data */
	private final TileMap terrain;
	
	/** Player and main observer */
	private Observer player;
	
	public World(File terrainData) throws IOException {
		try {
			terrain = new TileMap(terrainData);
		} catch (IOException e) {
			System.err.println("Failed to load terrain.");
			throw e;
		}
		player = new Observer(4, 4);
	}
	
	public void draw(Graphics g) {
		terrain.draw(g, player);
		player.draw(g, player);
	}
	
	public void movePlayer(double x, double y) {
		player.move(x,y);
	}
}
