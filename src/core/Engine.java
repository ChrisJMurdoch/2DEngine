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
	
	/** All game data */
	private final World world;
	
	/** Engine window */
	private final Display display;
	
	/** KeyListener implementation */
	private final KeyHandler keyboard;
	
	/** Queue to store frame durations in nanoseconds */
	private final long[] fps;
	private int fpsIndex = 0;
	
	private Engine() throws IOException {
		
		// Load configuration data
		Map<String, String> config = LoaderUtility.loadTextMap(new File("assets//Config.txt"));
		
		// Load world
		world = new World(config, new File("assets//TerrainData//Castle"));
		
		// Create display
		display = new Display(
			world,
			Integer.parseInt(config.get("window_width")),
			Integer.parseInt(config.get("window_height"))
		);
		
		// Create and add listeners
		display.addKeyListener(keyboard = new KeyHandler());
		
		// Create framerate queue
		fps = new long[500];
	}
	
	private void run() {
		
		// Main engine loop
		while (!keyboard.pressing(KeyEvent.VK_ESCAPE)) {
			
			// Get start-time
			long start = System.nanoTime();
			
			// Render
			display.syncRender();
			
			// Add to framerate
			long duration = System.nanoTime() - start;
			fps[fpsIndex++] = duration;
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
