package world;

public class World {
	
	private final String[] data;
	
	public World(String[] data) {
		this.data = data;
	}
	
	public void print() {
		for (String i : data) {
			System.out.println(i);
		}
	}
}
