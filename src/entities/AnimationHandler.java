package entities;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

public class AnimationHandler {
	
	private final Animation[] animations;
	
	public AnimationHandler(File file, long baseTime, int scale) throws IOException {
		File[] subFiles = file.listFiles();
		animations = new Animation[subFiles.length-1];
		int index = 0;
		for (int i=0; i<animations.length; i++)
			if (subFiles[i].isDirectory()) {
				animations[index++] = new Animation(subFiles[i], baseTime, scale);
		}
	}
	private AnimationHandler(Animation[] animations) {
		this.animations = animations;
	}
	
	public AnimationHandler clone() {
		Animation[] cloned = new Animation[animations.length];
		for (int i=0; i<animations.length; i++) {
			cloned[i] = animations[i];
	    }
		return new AnimationHandler(cloned);
	}

	public void draw(Graphics g, int xOff, int yOff, long currentTime, int animationCode) {
		animations[animationCode].draw(g, xOff, yOff, currentTime);
	}
}
