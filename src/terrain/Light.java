package terrain;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import core.MainEngine;

public class Light {
	
	private BufferedImage lightmap;
	private float radius;
	
	protected Light(float rad, BufferedImage shadowmap) {
		
		// Scale radius
		radius = rad * MainEngine.UNIT;
		
		// Create light paint
		RadialGradientPaint gradient = new RadialGradientPaint(
			new Point2D.Double( radius, radius ),
			radius,
			new float[] { 0, 1 },
			new Color[] { new Color(0.0f, 0.0f, 0.0f, 1.0f), new Color(0.0f, 0.0f, 0.0f, 0f) }
		);
		
		// Create image
		int dimension = (int)radius * 2;
		lightmap = new BufferedImage( dimension, dimension, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g = (Graphics2D)lightmap.getGraphics();
		
		// Draw light
		g.setPaint(gradient);
		g.fillRect(0, 0, dimension, dimension);
		
		// Allow light to pass through objects a little
		Graphics2D s = (Graphics2D)shadowmap.getGraphics();
		s.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
		s.setColor( new Color(0, 0, 0, MainEngine.SHADOW_SEEPING) );
		s.fillRect( 0, 0, shadowmap.getWidth(), shadowmap.getHeight() );
		
		// Remove light from shadows
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT));
		g.drawImage(shadowmap, 0, 0, null);
	}
	
	protected void draw(Graphics g, int x, int y) {
		g.drawImage(lightmap, (int)( x - radius ), (int)(y - radius ), null);
	}
}
