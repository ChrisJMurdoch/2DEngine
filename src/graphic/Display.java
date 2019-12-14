package graphic;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel {
	
	private static final long serialVersionUID = -4770893642896180196L;
	
	private final int WIDTH, HEIGHT;
	private final JFrame frame;
	
	public Display(int width, int height) {
		
		WIDTH = width;
		HEIGHT = height;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(0, 0, WIDTH, HEIGHT);
		frame.setUndecorated(true);
		frame.setLayout(null);
		
		setBounds(0, 0, WIDTH, HEIGHT);
		setFocusable(true);
		
		frame.add(this);
		frame.setVisible(true);
	}
}
