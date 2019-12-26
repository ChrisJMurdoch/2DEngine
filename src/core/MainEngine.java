package core;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import graphic.Display;
import input.KeyHandler;
import resource.LoaderUtility;
import world.World;

public class MainEngine extends AbstractEngine {
	
	
	// User configurations
	
	public static final int WINDOW_WIDTH;
	public static final int WINDOW_HEIGHT;
	/** Visual unit of measure equal to one tile */
	public static final int UNIT;
	/** Scroll buffer at screen edges */
	public static final int BORDER;
	
	
	// Engine references
	
	/** Game data */
	private World world;
	/** Engine window */
	private Display display;
	/** Key listener */
	private KeyHandler keyboard;
	
	
	// Initialise static constants
	static {
		Map<String, String> config = null;
		try {
			config = LoaderUtility.loadTextMap(new File("assets//Config.txt"));
		} catch(IOException e) {
			System.err.println("Failed to read config file.");
			e.printStackTrace();
		}
		WINDOW_WIDTH = Integer.parseInt(config.get("window_width"));
		WINDOW_HEIGHT = Integer.parseInt(config.get("window_height"));
		UNIT = WINDOW_WIDTH / Integer.parseInt(config.get("horizontal_tile_density"));
		BORDER = Integer.parseInt(config.get("border"));
	}
	
	private MainEngine() {
		super();
		try {
			world = new World(new File("assets//TerrainData//Castle"));
			display = new Display(world);
			display.addKeyListener(keyboard = new KeyHandler());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		start();
	}

	@Override
	public void tick(double secondsElapsed) {
		
		// Check exit key
		if (keyboard.pressing(KeyEvent.VK_ESCAPE))
			running = false;
		
		// Respond to user-input
		double metresPerSecond = 2;
		if (keyboard.pressing(KeyEvent.VK_W))
			world.movePlayer(0, -secondsElapsed*metresPerSecond);
		if (keyboard.pressing(KeyEvent.VK_A))
			world.movePlayer(-secondsElapsed*metresPerSecond, 0);
		if (keyboard.pressing(KeyEvent.VK_S))
			world.movePlayer(0, secondsElapsed*metresPerSecond);
		if (keyboard.pressing(KeyEvent.VK_D))
			world.movePlayer(secondsElapsed*metresPerSecond, 0);
		
		// Update display
		Debug.DEBUG_ONE = "Main FPS: " + getFrameRate();
		
		// Render in-thread
		display.paintComponent();
	}
	
	@Override
	public void end() {
		display.disposeFrame();
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		MainEngine engine = new MainEngine();
	}
}