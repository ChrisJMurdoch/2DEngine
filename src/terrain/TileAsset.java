package terrain;

import java.awt.Graphics;
import java.awt.Image;

import core.MainEngine;

/** Data loaded from external file, can be altered centrally */
public class TileAsset {
	
	/** Code used for aesthetic merge checks */
	private final int code;
	/** Saved images scaled to window size */
	private Image[] images;
	/** Codes of tiles this aesthetically merges with */
	private int[] merges;
	
	protected TileAsset(int code, Image[] images, int[] merges) {
		this.code = code;
		this.merges = merges;
		// Shallow copy to scaled
		this.images = new Image[images.length];
		for (int i=0; i< images.length; i++) {
			this.images[i] = images[i].getScaledInstance(MainEngine.UNIT, MainEngine.UNIT, Image.SCALE_DEFAULT);
		}
	}
	
	protected void draw(Graphics g, int x, int y, int orientation) {
		try {
			g.drawImage(images[orientation], x, y, null);
		} catch(ArrayIndexOutOfBoundsException e) {
			// Orientation not represented
			g.drawImage(images[0], x, y, null);
		}
	}
	
	/** Return image of given index */
	protected Image get(int index) {
		try {
			return images[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return images[0];
		}
	}
	
	/** Check whether this merges with given asset tile */
	protected boolean merges(TileAsset asset) {
		if (asset.code == code) 
			return true;
		for (int i : merges) {
			if (i == asset.code) {
				return true;
			}
		}
		return false;
	}
	
	public int getCode() {
		return code;
	}
}