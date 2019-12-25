package entities;

import core.Engine;

public class Observer extends Sprite {

	public Observer(double x, double y, double width, double height) {
		super(x, y, width, height);
	}
	
	/** Topleft corner co-ordinates, when centre-screen */
	public int xOffset() {
		return (int) ( ( x + (width / 2) ) * Engine.UNIT ) - ( Engine.WINDOW_WIDTH / 2 );
	}
	/** Topleft corner co-ordinates, when centre-screen */
	public int yOffset() {
		return (int) ( ( y + (height / 2) ) * Engine.UNIT ) - ( Engine.WINDOW_HEIGHT / 2 );
	}
}
