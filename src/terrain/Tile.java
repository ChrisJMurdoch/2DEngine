package terrain;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import core.MainEngine;

public class Tile {
	
	/** Data loaded from file */
	private TileAsset asset;
	
	/** Aesthetic orientation of tile, indexes into asset array */
	private int orientationCode;
	/** Image of tile with light baked-in */
	private BufferedImage baked;
	/** Lightmap if this is a lightsource, null otherwise */
	protected Light light;
	
	protected Tile(TileAsset asset) {
		this.asset = asset;
	}
	
	protected void setOrientation(int orientationCode) {
		this.orientationCode = orientationCode;
	}
	
	protected void draw(Graphics g, int x, int y) {
		g.drawImage( baked, x, y, null );
	}
	
	protected void update(BufferedImage lightmap) {
		baked = new BufferedImage( MainEngine.UNIT, MainEngine.UNIT, BufferedImage.TYPE_INT_RGB );
		Graphics g = baked.getGraphics();
		// Draw base image
		asset.draw( g, orientationCode );
		// Overlay lighting;
		g.drawImage(lightmap, 0, 0, null);
	}
	
	protected TileAsset getAsset() {
		return asset;
	}
}
