package core;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import graphic.Display;
import input.KeyHandler;
import resource.LoaderUtility;
import world.World;

public class Engine {
	
	/** User configurations */
	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;
	/** Global unit of measure equal to one tile */
	public static int UNIT;
	
	/** All game data */
	private final World world;
	
	/** Engine window */
	private final Display display;
	
	/** KeyListener implementation */
	private final KeyHandler keyboard;
	
	/** Queue to store frame durations in nanoseconds */
	private final long[] fps;
	private int fpsIndex = 0;
	
	/** Used for delta-time */
	private long lastTick;
	
	private Engine() throws IOException {
		
		// Load configuration data
		Map<String, String> config = LoaderUtility.loadTextMap(new File("assets//Config.txt"));
		WINDOW_WIDTH = Integer.parseInt(config.get("window_width"));
		WINDOW_HEIGHT = Integer.parseInt(config.get("window_height"));
		int density = Integer.parseInt(config.get("horizontal_tile_density"));
		UNIT = WINDOW_WIDTH / density;
		
		// Load world
		world = new World(new File("assets//TerrainData//Castle"));
		
		// Create display
		display = new Display(world);
		
		// Create and add listeners
		display.addKeyListener(keyboard = new KeyHandler());
		
		// Create framerate queue
		fps = new long[500];
	}
	
	private void run() {
		
		lastTick = System.nanoTime() - 10000000;
		
		// Main engine loop
		while (!keyboard.pressing(KeyEvent.VK_ESCAPE)) {
			
			// Get start-time
			long start = System.nanoTime();
			
			// Get time-delta
			long elapsed = start - lastTick;
			double second = (double)elapsed / 1000000000;
			
			// Controls
			double metresPerSecond = 2;
			if (keyboard.pressing(KeyEvent.VK_W))
				world.movePlayer(0, -second*metresPerSecond);
			if (keyboard.pressing(KeyEvent.VK_A))
				world.movePlayer(-second*metresPerSecond, 0);
			if (keyboard.pressing(KeyEvent.VK_S))
				world.movePlayer(0, second*metresPerSecond);
			if (keyboard.pressing(KeyEvent.VK_D))
				world.movePlayer(second*metresPerSecond, 0);
			
			// Render
			display.paintComponent();
			
			// Add to framerate
			fps[fpsIndex++] = elapsed;
			fpsIndex %= fps.length;
			
			// Get average framerate
			int counted = 0;
			long total = 0;
			for (long i : fps) {
				if (i == 0) {
					break;
				} else {
					counted++;
				}
				total += i;
			}
			
			// Display framerate
			long averageTickNano = total / counted;
			Debug.DEBUG_ONE = "Hz: " + 1000000000 / averageTickNano;
			
			// Set tick
			lastTick = start;
		}
		
		// Close window
		display.disposeFrame();
	}
	
	public static void main(String[] args) {
		
		try {
			// Load engine
			Engine engine = new Engine();
			
			// Run engine
			engine.run();
			
		} catch (IOException e) {
			System.err.println("Failed load game data.");
			e.printStackTrace();
		}
	}
}
