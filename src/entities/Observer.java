package entities;

import core.MainEngine;

/** Sprite with ability to have screen focused on body centre */
public class Observer extends Sprite {

	public Observer(double x, double y, SpriteAsset asset) {
		super(x, y, asset);
	}
	
	/** Topleft corner co-ordinates, when centre-screen */
	public int xOffset() {
		return (int) ( ( x + (asset.width / 2) ) * MainEngine.UNIT ) - ( MainEngine.WINDOW_WIDTH / 2 );
	}
	
	/** Topleft corner co-ordinates, when centre-screen */
	public int yOffset() {
		return (int) ( ( y + (asset.height / 2) ) * MainEngine.UNIT ) - ( MainEngine.WINDOW_HEIGHT / 2 );
	}
}
