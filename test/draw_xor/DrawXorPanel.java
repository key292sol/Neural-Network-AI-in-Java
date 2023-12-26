package test.draw_xor;

import java.awt.Graphics;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import ai.NeuralNetwork;

public class DrawXorPanel extends JPanel {

	final int SIZE = 9;

	double[][] data = {
			{ 0, 0 },
			{ 0, 1 },
			{ 1, 0 },
			{ 1, 1 }
	};

	double[][] answers = {
			{ 0 }, { 1 }, { 1 }, { 0 }
	};

	NeuralNetwork nn;

	boolean started = false;

	public DrawXorPanel() {
		nn = new NeuralNetwork(2, 1, 1, 2, 0.1);
	}

	private void sleepme(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		if (!started) {
			started = true;
		}

		// sleepme(500);

		final int WIDTH = getWidth();
		final int HEIGHT = getHeight();

		for (int i = 0; i < WIDTH; i += SIZE) {
			for (int j = SIZE; j < HEIGHT; j += SIZE) {
				double x = (i * 1.0) / WIDTH;
				double y = (j * 1.0) / HEIGHT;
				double[] inputs = { x, y };

				float op = (float) (nn.predict(inputs)[0]);

				Color c = new ColorUIResource(op, op, op);
				// c = Color.BLACK;

				g.setColor(c);
				g.fillRect(i, j, SIZE, SIZE);

				// g.setColor(Color.BLACK);
				// op = ((float) Math.round(op * 100)) / 100;
				// g.drawString(Float.toString(op), i, j);
			}
		}

		for (int i = 0; i < 20; i++) {
			nn.train(data, answers);
		}

		repaint();
	}
}
