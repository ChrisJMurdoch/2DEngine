package resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		reader.close();
		return lines;
	}
}
