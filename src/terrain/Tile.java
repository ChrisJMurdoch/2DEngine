package terrain;

import java.awt.Graphics;

public class Tile {
	
	/** Data loaded from file */
	private TileAsset asset;
	/** Aesthetic orientation of tile, indexes into asset array */
	private int orientationCode;
	
	protected  Tile(TileAsset asset) {
		this.asset = asset;
	}
	
	protected void setOrientation(int orientationCode) {
		this.orientationCode = orientationCode;
	}
	
	protected void draw(Graphics g, int x, int y) {
		asset.draw(g, x, y, orientationCode);
	}
	
	protected TileAsset getAsset() {
		return asset;
	}
}
