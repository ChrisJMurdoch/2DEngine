package terrain;

import java.awt.image.BufferedImage;

import core.Debug;
import core.Engine;
import entities.Observer;

public class UpdateEngine implements Runnable {
	
	// References
	private TileMap map;
	
	// Update information
	private Observer observer;
	private boolean update;
	
	// Images
	private BufferedImage one;
	private BufferedImage two;
	private BufferedImage ready;
	
	protected UpdateEngine(TileMap map) {
		
		this.map = map;
		
		update = false;
		
		one = new BufferedImage(Engine.WINDOW_WIDTH + Engine.BORDER*2, Engine.WINDOW_HEIGHT + Engine.BORDER*2, BufferedImage.TYPE_INT_RGB);
		two = new BufferedImage(Engine.WINDOW_WIDTH + Engine.BORDER*2, Engine.WINDOW_HEIGHT + Engine.BORDER*2, BufferedImage.TYPE_INT_RGB);
		ready = one;
		
		Thread updateThread = new Thread(this);
		updateThread.start();
	}
	
	protected void queueUpdate(Observer observer) {
		this.observer = observer;
		update = true;
	}
	
	protected BufferedImage getImage() {
		return ready;
	}
	
	@Override
	public void run() {
		
		while (Engine.running) {
			
			if (update) {
				update = false;
				
				// Determine image to update
				if (ready == one) {
					map.update(two.getGraphics(), observer);
					Debug.DEBUG_THREE = "TWO";
					ready = two;
				} else {
					map.update(one.getGraphics(), observer);
					Debug.DEBUG_THREE = "ONE";
					ready = one;
				}
			}
			
			// Limit cpu usage
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
