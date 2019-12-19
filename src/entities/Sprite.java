package entities;

import java.awt.Color;
import java.awt.Graphics;

import core.Engine;

public class Sprite {
	
	private double x;
	private double y;
	
	public Sprite(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g, Observer observer) {
		g.setColor(Color.MAGENTA);
		g.drawRect((int)(x*Engine.UNIT-observer.xOffset()), (int)(y*Engine.UNIT-observer.yOffset()), 5, 5);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void move(double x, double y) {
		this.x += x;
		this.y += y;
	}
}
