package test.draw_network;

import javax.swing.JFrame;

public class DrawNetFrame extends JFrame {
	public DrawNetFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);

		add(new DrawNetPanel());
	}
}
