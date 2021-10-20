package ai;

import ai.math.Matrix;

public class NeuralNetwork {
	// private double[][][] weights;
	private int numInputs, numOps, numHiddenNodes, numHiddenLayers;
	private double learningRate;

	private Matrix[] weights;
	private Matrix[] biases;
	private Matrix[] layers;

	public NeuralNetwork (int inputs, int ops, int hiddenLayers, int hiddenNodesInLayer) {
		this(inputs, ops, hiddenLayers, hiddenNodesInLayer, 0.5);
	}

	public NeuralNetwork (int inputs, int ops, int hiddenLayers, int hiddenNodesInLayer, double _learningRate) {
		if (inputs < 1 || ops < 1 ||  (hiddenLayers > 0 && hiddenNodesInLayer < 1)) {
			System.out.println("WTF?");
			System.out.println("Give proper parameters to constructor");
			return;
		}

		this.numInputs = inputs;
		this.numOps = ops;
		this.numHiddenLayers = hiddenLayers;
		this.numHiddenNodes = hiddenNodesInLayer;
		this.learningRate = _learningRate;

		this.layers = new Matrix[hiddenLayers + 2];
		this.weights = new Matrix[hiddenLayers + 1];
		this.biases = new Matrix[hiddenLayers + 1];

		// Weights and Biases
		weights[0] = new Matrix(hiddenNodesInLayer, inputs);
		weights[hiddenLayers] = new Matrix(ops, hiddenNodesInLayer);

		biases[0] = new Matrix(inputs, 1);
		biases[hiddenLayers] = new Matrix(ops, 1);

		for (int i = 1; i < hiddenLayers; i++) {
			weights[i] = new Matrix(hiddenNodesInLayer, hiddenNodesInLayer);
			biases[i] = new Matrix(hiddenNodesInLayer, 1);
		}
	}
	
	private double sigmoid(double n) {
		return (1 / (1 + Math.exp(-n)));
	}

	private double sigmoidDerivative(double n) {
		double sig = sigmoid(n);
		return (sig * (1 - sig));
	}

	// When the number n is already "sigmoided"
	private double sigmoidDerivative2(double n) {
		return (n * (1 - n));
	}

	// Store activations so that we can do backpropagation in training
	private double[] feedForward(double[] inputs, boolean storeLayers) {
		/* if (inputs.length != numInputs) {
			System.err.println("Inputs length and initialised inputs length is not the same");
			return null;
		} */

		// Convert inputs to matrix
		Matrix curLayer = Matrix.fromArray(inputs);

		if (storeLayers) {
			layers[0] = curLayer;
		}

		// Loop through weights and hiddenLayers to generate a guess
		for (int i = 0; i < weights.length; i++) {
			// OP = sig(W.I + B)
			curLayer = Matrix.multiply(weights[i], curLayer); // W.I
			curLayer = Matrix.add(curLayer, biases[i]);  // + B
			curLayer = Matrix.mapElements(curLayer, this::sigmoid);  // sig(x)

			if (storeLayers) {
				layers[i + 1] = curLayer;
			}
		}

		// Return the guess
		return curLayer.toArray();
	}

	public double[] feedForward(double[] inputs) {
		return feedForward(inputs, false);
	}

	private void backPropagation(double[] answers) {
		Matrix myOp = layers[layers.length - 1];
		Matrix ansMatrix = Matrix.fromArray(answers);

		// Calculate the errors
		Matrix opErrors = Matrix.subtract(myOp, ansMatrix);
		// Matrix opErrors = Matrix.subtract(ansMatrix, myOp);

		Matrix inpErrors = null;

		for (int i = weights.length - 1; i >= 0; i--) {
			// Generate errors of the input to this layer
			if (i != 0) {
				Matrix weightsTrans = Matrix.transpose(weights[i]);
				inpErrors = Matrix.multiply(weightsTrans, opErrors);
			}

			// Transpose of the inputs
			Matrix inpTrans = Matrix.transpose(layers[i]);

			// Derivative of the output of this layer / weights
			Matrix opDerivative = Matrix.mapElements(layers[i + 1], this::sigmoidDerivative2);

			// biasDelta    = lr * E * dx(O)
			Matrix biasDelta = Matrix.multiply(opErrors, this.learningRate);
			biasDelta = Matrix.multiplyElements(biasDelta, opDerivative);

			// weightsDelta = lr * E * dx(O) . inpTranspose
			Matrix weightsDelta = Matrix.multiply(biasDelta, inpTrans);

			// Add deltas to the weights and biases
			biases[i] = Matrix.add(biases[i], biasDelta);
			weights[i] = Matrix.add(weights[i], weightsDelta);

			// This layers inpErrors are previous layers opErrors
			opErrors = inpErrors;
		}

		layers = new Matrix[layers.length];
	}



	public void train(double[] inputs, double[] answers) {
		// Predict
		feedForward(inputs, true);

		// Backpropagate and change weights
		this.backPropagation(answers);
	}

	public void train(double[][] inputs, double[][] answers) {
		for (int i = 0; i < inputs.length; i++) {
			this.train(inputs[i], answers[i]);
		}
	}

	public double[] predict(double[] inputs) {
		return this.feedForward(inputs);
	}
}