package entities;

import core.MainEngine;

/** Sprite with ability to have screen focused on body centre */
public class Observer extends Sprite {

	public Observer(double x, double y, double width, double height, SpriteAsset asset) {
		super(x, y, width, height, asset);
	}
	
	/** Topleft corner co-ordinates, when centre-screen */
	public int xOffset() {
		return (int) ( ( x + (width / 2) ) * MainEngine.UNIT ) - ( MainEngine.WINDOW_WIDTH / 2 );
	}
	
	/** Topleft corner co-ordinates, when centre-screen */
	public int yOffset() {
		return (int) ( ( y + (height / 2) ) * MainEngine.UNIT ) - ( MainEngine.WINDOW_HEIGHT / 2 );
	}
}
