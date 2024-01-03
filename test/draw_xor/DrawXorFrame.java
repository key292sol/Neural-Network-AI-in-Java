package test.draw_xor;

import javax.swing.JFrame;

public class DrawXorFrame extends JFrame {
	public DrawXorFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 400);
		add(new DrawXorPanel());
		setVisible(true);

	}
}