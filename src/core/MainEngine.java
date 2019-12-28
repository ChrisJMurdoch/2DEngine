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
	
	public static final boolean DEBUG;
	public static final int WINDOW_WIDTH;
	public static final int WINDOW_HEIGHT;
	/** Visual unit of measure equal to one tile */
	public static final int UNIT;
	/** 0 = Dark  -  255 = Light */
	public static final int GLOBAL_ALPHA;
	/** How many tiles away a tile will check for light source */
	public static final int MAX_ILLUMINATION_RADIUS;
	/** How much light will penetrate opaque walls */
	public static final int SHADOW_SEEPING;
	/** Super sample anti-aliasing factor; 1 = Normal resolution, 2 = 2x resolution */
	public static final double SSAA;
	
	
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
		DEBUG = config.get("debug").equals("true");
		WINDOW_WIDTH = Integer.parseInt(config.get("window_width"));
		WINDOW_HEIGHT = Integer.parseInt(config.get("window_height"));
		UNIT = WINDOW_WIDTH / Integer.parseInt(config.get("horizontal_tile_density"));
		GLOBAL_ALPHA = Integer.parseInt(config.get("global_alpha"));
		MAX_ILLUMINATION_RADIUS = Integer.parseInt(config.get("max_illumination_radius"));
		SHADOW_SEEPING = Integer.parseInt(config.get("shadow_seeping"));
		SSAA = Double.parseDouble(config.get("ssaa"));
	}
	
	private MainEngine() {
		super();
		try {
			world = new World(new File("assets//TerrainData//Castle"), new File("assets//SpriteData") );
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
		double x = 0, y = 0;
		double metresPerSecond = 2;
		if (keyboard.pressing(KeyEvent.VK_W))
			y--;
		if (keyboard.pressing(KeyEvent.VK_A))
			x--;
		if (keyboard.pressing(KeyEvent.VK_S))
			y++;
		if (keyboard.pressing(KeyEvent.VK_D))
			x++;
		
		// Move player
		world.movePlayer(x*metresPerSecond*secondsElapsed, y*metresPerSecond*secondsElapsed);
		
		// Update display
		Debug.DEBUG_ONE = "FPS: " + getFrameRate();
		
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
