package graphic;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Debug;
import world.World;

public class Display extends JPanel {
	
	private static final long serialVersionUID = -4770893642896180196L;
	
	private final JFrame frame;
	private final World world;
	
	/** Render sync variable */
	private boolean renderReady = true;
	
	public Display(World world, int width, int height) {
		
		this.world = world;
		
		// Used for centering screen
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		// Create JFrame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds( (screenWidth-width)/2, (screenHeight-height)/2, width, height );
		frame.setUndecorated(true);
		frame.setLayout(new BorderLayout());
		
		// Create panel
		setBounds(0, 0, width, height);
		setFocusable(true);
		
		// Display
		frame.add(this);
		frame.setVisible(true);
	}
	
	/** Render without request pooling */
	public void syncRender() {
		
		// If rendering
		if (!renderReady) {
			// Wait for end notification
			try {
				synchronized(this) {
					wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		renderReady = false;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		// Draw world
		world.draw(g, getWidth(), getHeight());
		
		// Draw HUD
		Debug.draw(g, 0, 0);
		
		// Finish and sync
		renderReady = true;
		synchronized(this) {
			notify();
		}
	}
	
	public void disposeFrame() {
		frame.dispose();
	}
}
