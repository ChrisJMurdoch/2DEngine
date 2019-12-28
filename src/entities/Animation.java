package entities;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import core.MainEngine;
import resource.LoaderUtility;

public class Animation {

	// Constants
	public static final int STANDING = 0;
	public static final int WALKING_N = 1;
	public static final int WALKING_E = 2;
	public static final int WALKING_S = 3;
	public static final int WALKING_W = 4;
	public static final int ATTACKING = 5;

	// Traits
	private final long baseTime;
	private final int stepDuration;
	private final Image[] images;

	public Animation(File file, long baseTime, int scale) throws IOException {
		File imageFile = new File(file.getPath() + "//Images");
		File dataFile = new File(file.getPath() + "//Data.txt");
		Map<String, String> data = LoaderUtility.loadTextMap(dataFile);
		this.baseTime = baseTime;
		this.stepDuration = Integer.parseInt(data.get("StepDuration"));
		this.images = LoaderUtility.loadImageArray(imageFile);
		// Scale
		for (int i=0; i<images.length; i++) {
			images[i] = images[i].getScaledInstance(scale+1, scale+1, Image.SCALE_DEFAULT);
		}
	}

	private int getStep(long currentTime) {
		long mils = currentTime - baseTime;
		long step = (mils - (mils % stepDuration)) / stepDuration;
		long stepNormalised = step % images.length;
		return (int)stepNormalised;
	}

	public void draw(Graphics g, int xOff, int yOff, long currentTime) {
		g.drawImage(images[getStep(currentTime)], xOff, yOff, null);
	}
}