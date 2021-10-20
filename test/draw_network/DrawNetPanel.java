package test.draw_network;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.plaf.ColorUIResource;

import java.awt.Color;

import ai.NeuralNetwork;

class Position {
	public int x, y;
}

public class DrawNetPanel extends JPanel {

	final int NODE_SIZE = 20;
	final int PADDING = 20;

	Position[][] nodeLocations;

	NeuralNetwork nn;

	public DrawNetPanel() {
		int numInputs = 2, 
			numOps = 1,
			numHiddenLayers = 1,
			numHiddenNodes = 2;

		nn = new NeuralNetwork(numInputs, numOps, numHiddenLayers, numHiddenNodes, 0.5);

		nodeLocations = new Position[numHiddenLayers + 2][];
		nodeLocations[0] = new Position[numInputs];
		nodeLocations[numHiddenLayers + 1] = new Position[numOps];
		for (int i = 0; i < numHiddenLayers; i++) {
			nodeLocations[i + 1] = new Position[numHiddenNodes];
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		final int DRAWING_WIDTH  = getWidth() - (2 * PADDING);
		final int DRAWING_HEIGHT = getHeight() - (2 * PADDING);

		// Color c = new ColorUIResource(0.3f, 0.3f, 0.3f);
	}

	private void initNodePositions(int drawWidth, int drawHeight) {
		int spacing = drawWidth / nodeLocations.length;

		for (int i = 0; i < nodeLocations.length; i++) {
			
		}
	}

	private int getAvg(int a, int b) {
		return ((a + b) / 2);
	}
}
