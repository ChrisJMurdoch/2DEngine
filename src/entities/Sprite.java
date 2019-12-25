package entities;

import java.awt.Color;
import java.awt.Graphics;

import core.MainEngine;

public class Sprite {
	
	protected double x, y, width, height;
	
	public Sprite(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g, Observer observer) {
		g.setColor(Color.MAGENTA);
		g.drawRect( (int)( (x * MainEngine.UNIT) - observer.xOffset()), (int)( (y * MainEngine.UNIT) - observer.yOffset()), (int)width * MainEngine.UNIT, (int)height * MainEngine.UNIT );
	}
	
	public void move(double x, double y) {
		this.x += x;
		this.y += y;
	}
}
