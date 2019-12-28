package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

import core.Debug;
import core.MainEngine;
import terrain.TileMap;

public class Sprite {
	
	protected SpriteAsset asset;
	protected double x, y;
	private int animationCode = 0;
	
	public Sprite(double x, double y, SpriteAsset asset) {
		this.x = x;
		this.y = y;
		this.asset = asset;
	}
	
	public void draw(Graphics g, Observer observer, long time) {
		g.setColor(Color.MAGENTA);
		asset.draw(g, (int)(x*MainEngine.UNIT-observer.xOffset()), (int)(y*MainEngine.UNIT-observer.yOffset()), time, animationCode);
	}
	
	public void move(double x, double y, TileMap terrain) {
		
		// Set visuals
		setAnimation(x, y);
		
		// Scale vector if diagonal
		if ( x!=0 && y!=0 ) {
			x /= Math.pow(2, 0.5);
			y /= Math.pow(2, 0.5);
		}
		
		// Save position
		double sx = this.x, sy = this.y;
		
		// Try move x
		double mult = 1;
		do {
			this.x = sx + (mult*x);
			mult -= 0.1;
		} while (terrain.isColliding(getCorners()));
		
		// Try move y
		mult = 1;
		do {
			this.y = sy + (mult*y);
			mult -= 0.1;
		} while (terrain.isColliding(getCorners()));
		
	}
	
	private void setAnimation(double x, double y) {
		if (y < 0) {
			animationCode = 1;
			return;
		}
		if (y > 0) {
			animationCode = 3;
			return;
		}
		if (x > 0) {
			animationCode = 2;
			return;
		}
		if (x < 0) {
			animationCode = 4;
			return;
		}
		animationCode = 0;
	}
	
	public Point2D[] getCorners() {
		return new Point2D[] {
				new Point2D.Double(x, y),
				new Point2D.Double(x+asset.width, y),
				new Point2D.Double(x+asset.width, y+asset.height),
				new Point2D.Double(x, y+asset.height)
		};
	}
}
