package terrain;

import java.awt.Graphics;
import java.awt.Image;

/** Data loaded from external file, can be altered centrally */
public class TileAsset {
	
	/** Code used for aesthetic merge checks */
	private final int code;
	/** Original images, immutable */
	private final Image[] images;
	/** Cached images scaled to window size */
	private Image[] scaled;
	/** Codes of tiles this aesthetically merges with */
	private int[] merges;
	
	protected TileAsset(int code, Image[] images, int[] merges) {
		this.code = code;
		this.images = images;
		this.merges = merges;
		// Shallow copy to scaled
		scaled = new Image[images.length];
		for (int i=0; i < images.length; i++) {
			scaled[i] = images[i];
		}
	}
	
	protected void draw(Graphics g, int x, int y, int orientation) {
		try {
			g.drawImage(scaled[orientation], x, y, null);
		} catch(ArrayIndexOutOfBoundsException e) {
			// Orientation not represented
			g.drawImage(scaled[0], x, y, null);
		}
	}
	
	protected int getSize() {
		return scaled[0].getWidth(null);
	}
	
	/** Scale all images to scaled array */
	protected void scale(int dimension) {
		for (int i=0; i< images.length; i++) {
			scaled[i] = images[i].getScaledInstance(dimension, dimension, Image.SCALE_DEFAULT);
		}
	}
	
	/** Return scaled image of given index */
	protected Image get(int index) {
		try {
			return scaled[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			return scaled[0];
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
}