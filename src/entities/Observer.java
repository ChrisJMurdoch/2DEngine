package entities;

import core.Engine;

public class Observer extends Sprite {

	public Observer(double x, double y) {
		super(x, y);
	}
	
	/** Topleft corner co-ordinates, when centre-screen */
	public int xOffset() {
		return (int)( getX()*Engine.UNIT ) - Engine.WINDOW_WIDTH / 2;
	}
	/** Topleft corner co-ordinates, when centre-screen */
	public int yOffset() {
		return (int)( getY()*Engine.UNIT ) - Engine.WINDOW_HEIGHT / 2;
	}
}
