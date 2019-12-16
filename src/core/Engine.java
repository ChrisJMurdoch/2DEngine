package core;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import graphic.Display;
import resource.LoaderUtility;
import world.World;

public class Engine {
	
	private final World world;
	private final Display display;
	
	private Engine() throws IOException {
		
		// Load configuration data
		Map<String, String> config = LoaderUtility.loadTextMap(new File("assets//Config.txt"));
		
		// Load world
		world = new World(config, new File("assets//TerrainData//Castle.txt"));
		
		// Create display
		display = new Display(
			world,
			Integer.parseInt(config.get("window_width")),
			Integer.parseInt(config.get("window_height"))
		);
	}
	
	private void run() {
		display.repaint();
	}
	
	public static void main(String[] args) {
		try {
			Engine engine = new Engine();
			engine.run();
		} catch (IOException e) {
			System.err.println("Failed load game data.");
			e.printStackTrace();
		}
	}
}
