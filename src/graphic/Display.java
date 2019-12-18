package graphic;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Debug;
import world.World;

public class Display extends JPanel {
	
	private static final long serialVersionUID = -4770893642896180196L;
	
	private final JFrame frame;
	private final World world;
	
	/** Used for double buffering */
	private BufferedImage buffer;
	private Graphics b;
	
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
		
		// Create buffer
		buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		b = buffer.getGraphics();
	}
	
	public void paintComponent() {
		paintComponent(getGraphics());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		// Draw world
		world.draw(b, getWidth(), getHeight());
		
		// Draw HUD
		Debug.draw(b, 0, 0);
		
		// Finish and sync
		g.drawImage(buffer, 0, 0, this);
	}
	
	public void disposeFrame() {
		frame.dispose();
	}
}
