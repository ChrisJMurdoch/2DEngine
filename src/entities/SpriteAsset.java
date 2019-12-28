package entities;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import core.MainEngine;
import resource.LoaderUtility;

public class SpriteAsset {
	
	private final AnimationHandler animations;
	protected final double width, height;
	
	public SpriteAsset(File file, long baseTime) throws IOException {
		File dataFile = new File(file.getPath() + "//Data.txt");
		Map<String, String> data = LoaderUtility.loadTextMap(dataFile);
		width = Double.parseDouble(data.get("width"));
		height = Double.parseDouble(data.get("height"));
		File animationFile = new File(file.getPath() + "//Animations");
		animations = new AnimationHandler(animationFile, baseTime, (int)(width*MainEngine.UNIT));
	}
	
	protected void draw(Graphics g, int x, int y, long time, int animationCode) {
		animations.draw(g, x, y, time, animationCode);
	}
}
