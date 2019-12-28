package entities;

import java.awt.Color;
import java.awt.Graphics;

import core.MainEngine;

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
	
	public void move(double x, double y) {
		this.x += x;
		this.y += y;
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
}
