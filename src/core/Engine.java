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
	
	private final World world;
	private final Display display;
	private final KeyHandler keyboard;
	
	private Engine() throws IOException {
		
		// Load configuration data
		Profiler.startProcess("ConfigLoad");
		Map<String, String> config = LoaderUtility.loadTextMap(new File("assets//Config.txt"));
		
		// Load world
		Profiler.startProcess("WorldLoad");
		world = new World(config, new File("assets//TerrainData//Castle.txt"));
		
		// Create display
		Profiler.startProcess("DisplayOpen");
		display = new Display(
			world,
			Integer.parseInt(config.get("window_width")),
			Integer.parseInt(config.get("window_height"))
		);
		
		// Create and add listeners
		Profiler.startProcess("ListenerAdd");
		display.addKeyListener(keyboard = new KeyHandler());
		
		// Dummy profile
		Profiler.startProcess("General");
	}
	
	private void run() {
		while (!keyboard.pressing(KeyEvent.VK_ESCAPE)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Close window
		display.disposeFrame();
	}
	
	public static void main(String[] args) {
		try {
			Engine engine = new Engine();
			Profiler.print();
			engine.run();
		} catch (IOException e) {
			System.err.println("Failed load game data.");
			e.printStackTrace();
		}
	}
}
