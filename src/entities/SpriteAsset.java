package entities;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import resource.LoaderUtility;

public class SpriteAsset {
	
	private final AnimationHandler animations;
	
	public SpriteAsset(File file, long baseTime) throws IOException {
		File dataFile = new File(file.getPath() + "//Data.txt");
		File animationFile = new File(file.getPath() + "//Animations");
		Map<String, String> data = LoaderUtility.loadTextMap(dataFile);
		animations = new AnimationHandler(animationFile, baseTime);
	}
	
	protected void draw(Graphics g, int x, int y, long time, int animationCode) {
		animations.draw(g, x, y, time, animationCode);
	}
}
