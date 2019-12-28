package graphic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import core.Debug;
import core.MainEngine;
import resource.LoaderUtility;
import world.World;

public class Display extends JPanel {
	
	private static final long serialVersionUID = -4770893642896180196L;
	
	// References
	private final JFrame frame;
	private final World world;
	
	// Image for double buffering
	private BufferedImage buffer;
	private Graphics b;
	
	public Display(World world) throws IOException {
		
		this.world = world;
		
		// Used for centering screen
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		// Create JFrame
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds( (screenWidth - MainEngine.WINDOW_WIDTH) / 2, (screenHeight - MainEngine.WINDOW_HEIGHT) / 2, MainEngine.WINDOW_WIDTH, MainEngine.WINDOW_HEIGHT );
		frame.setUndecorated(true);
		frame.setLayout(new BorderLayout());
		
		// Create panel
		setBounds(0, 0, MainEngine.WINDOW_WIDTH, MainEngine.WINDOW_HEIGHT);
		setFocusable(true);
		
		// Display
		frame.add(this);
		frame.setVisible(true);
		
		// Create buffer
		buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		b = buffer.getGraphics();
		
		// Set cursor
		BufferedImage cursorImg = (BufferedImage) LoaderUtility.loadImage(new File("assets\\Misc\\Cursor.png"));
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "Cursor");
		frame.getContentPane().setCursor(cursor);
	}
	
	public void paintComponent() {
		paintComponent(getGraphics());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		// Clear
		b.setColor(Color.BLACK);
		b.fillRect(0, 0, MainEngine.WINDOW_WIDTH, MainEngine.WINDOW_HEIGHT);
		
		// Draw world
		world.draw(b);
		
		// Draw Debug display
		if (MainEngine.DEBUG)
			Debug.draw(b, 5, 5);
		
		// Draw to screen
		g.drawImage(buffer, 0, 0, this);
	}
	
	public void disposeFrame() {
		frame.dispose();
	}
}
