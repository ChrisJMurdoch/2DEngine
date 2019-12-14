package core;

import java.io.File;
import java.io.IOException;

import resource.LoaderUtility;
import world.World;

public class Engine {
	
	private final World world;
	
	private Engine() throws IOException {
		String[] worldData;
		worldData = LoaderUtility.loadTextFile(new File("assets//World.txt"));
		world = new World(worldData);
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
