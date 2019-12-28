package terrain;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import core.MainEngine;

/** Data loaded from external file, can be altered centrally */
public class TileAsset {
	
	/** Code used for aesthetic merge checks */
	private final int code;
	/** Codes of tiles this aesthetically merges with */
	private final int[] merges;
	protected final int illuminationRadius;
	
	/** Saved images scaled to window size */
	private Image[] images;
	
	protected TileAsset(int code, Image[] images, Map<String, String> settings) {
		this.code = code;
		
		// Get merges
		String[] stringMerges = settings.get("merges").split(",");
		merges = new int[stringMerges.length];
		for (int j=0; j < stringMerges.length; j++) {
			merges[j] = Integer.parseInt(stringMerges[j]);
		}
		
		// Get illumaination
		illuminationRadius = Integer.parseInt(settings.get("illumination"));
		
		// Scale images
		this.images = new Image[images.length];
		for (int i=0; i< images.length; i++) {
			this.images[i] = images[i].getScaledInstance(MainEngine.UNIT, MainEngine.UNIT, Image.SCALE_DEFAULT);
		}
	}
	
	protected void draw(Graphics g, int orientation) {
		try {
			g.drawImage(images[orientation], 0, 0, null);
		} catch(ArrayIndexOutOfBoundsException e) {
			// Orientation not represented
			g.drawImage(images[0], 0, 0, null);
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