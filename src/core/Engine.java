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
		world = new World(LoaderUtility.loadTextFile(new File("assets//World.txt")));
		// Load display
		display = new Display(
			Integer.parseInt(config.get("window_width")),
			Integer.parseInt(config.get("window_height"))
		);
	}
	
	private void run() {
		world.print();
	}
	
	public static void main(String[] args) {
		try {
			Engine engine = new Engine();
			engine.run();
		} catch (IOException e) {
			System.err.println("Failed to read game data.");
			e.printStackTrace();
		}
	}
}
