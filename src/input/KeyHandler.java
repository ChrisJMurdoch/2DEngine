package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler implements KeyListener {

	private Set<Integer> activeKeys;
	
	public KeyHandler() {
		activeKeys = new HashSet<>();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		activeKeys.add(e.getKeyCode());
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		activeKeys.remove(e.getKeyCode());
	}
	
	public boolean pressing(Integer in) {
		return activeKeys.contains(in);
	}
}