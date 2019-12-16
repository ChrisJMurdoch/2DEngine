package resource;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class LoaderUtility {

	/** Return text file as String array of lines */
	public static String[] loadTextFile(File file) throws IOException {
		
		// Create reader
		BufferedReader reader;
		int lineCount = 0;
		
		//Count lines
		reader = new BufferedReader(new FileReader(file));
		while (reader.readLine() != null) {
			lineCount++;
		}
		reader.close();
		
		//Read lines
		reader = new BufferedReader(new FileReader(file));
		String[] lines = new String[lineCount];
		for (int i = 0; i < lines.length; i++) {
			lines[i] = reader.readLine();
		}
		
		// Finish
		reader.close();
		return lines;
	}

	/** Load image in file */
	public static Image loadImage(File file) throws IOException {
		return ImageIO.read(file);
	}

	/** Load image in file */
	public static Image loadImage(File file, int width, int height) throws IOException {
		return ImageIO.read(file).getScaledInstance(width, height, Image.SCALE_DEFAULT);
	}
	
	/** Loads folder of images as Image array */
	public static Image[] loadImageArray(File file) throws IOException {
		File[] subs = file.listFiles();
		Image[] images = new Image[subs.length];
		for (int i=0; i< subs.length; i++) {
			images[i] = loadImage(subs[i]);
		}
		return images;
	}
	
	/** Loads folder of images as Image array */
	public static Image[] loadImageArray(File file, int width, int height) throws IOException {
		File[] subs = file.listFiles();
		Image[] images = new Image[subs.length];
		for (int i=0; i< subs.length; i++) {
			images[i] = loadImage(subs[i], width, height);
		}
		return images;
	}
	
	/** Return Map of key:value pairs */
	public static Map<String, String> loadTextMap(File file) throws IOException {
		
		// Get file lines
		String[] data = loadTextFile(file);
		
		// Map data
		Map<String, String> map = new HashMap<>();
		for (String i : data) {
			String[] split = i.split(":");
			map.put(split[0], split[1]);
		}
		
		return map;
	}
}
