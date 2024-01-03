package ai;

import java.util.function.DoubleFunction;

import ai.math.Matrix;

public class NeuralNetwork {
	public enum OpType {
		REGRESSION,
		BOOL_CLASSIFICATION,
		SINGLE_CLASSIFICATION,
		MULILABEL_CLASSIFICATION
	}

	// private int numInputs, numOps, numHiddenNodes, numHiddenLayers;

	private Matrix[] layers;

	private DoubleFunction<Double> hiddenActivationFunction, hiddenDerivativeFunction;
	private DoubleFunction<Double> opActivationFunction, opDerivativeFunction;

	public OpType opType;
	public double learningRate;

	public Matrix[] weights;
	public Matrix[] biases;

	public NeuralNetwork(int inputs, int ops, int hiddenLayers, int hiddenNodesInLayer) {
		this(inputs, ops, hiddenLayers, hiddenNodesInLayer, OpType.BOOL_CLASSIFICATION);
	}

	public NeuralNetwork(int inputs, int ops, int hiddenLayers, int hiddenNodesInLayer, OpType _opType) {
		this(inputs, ops, hiddenLayers, hiddenNodesInLayer, _opType, 0.3);
	}

	public NeuralNetwork(int inputs, int ops, int hiddenLayers, int hiddenNodesInLayer, OpType _opType,
			double _learningRate) {
		if (inputs < 1 || ops < 1 || (hiddenLayers > 0 && hiddenNodesInLayer < 1)) {
			System.out.println("Give proper parameters to constructor");
			return;
		}

		this.opType = _opType;
		this.learningRate = _learningRate;

		this.layers = new Matrix[hiddenLayers + 2];
		this.weights = new Matrix[hiddenLayers + 1];
		this.biases = new Matrix[hiddenLayers + 1];

		// Weights and Biases
		weights[0] = new Matrix(hiddenNodesInLayer, inputs);
		weights[hiddenLayers] = new Matrix(ops, hiddenNodesInLayer);

		biases[0] = new Matrix(hiddenNodesInLayer, 1);
		biases[hiddenLayers] = new Matrix(ops, 1);

		for (int i = 1; i < hiddenLayers; i++) {
			weights[i] = new Matrix(hiddenNodesInLayer, hiddenNodesInLayer);
			biases[i] = new Matrix(hiddenNodesInLayer, 1);
		}

		// Set Actication Functions
		switch (_opType) {
			case REGRESSION:
				this.hiddenActivationFunction = ActivationFunctions::relu;
				this.hiddenDerivativeFunction = DerivativeFunctions::reluDerivative;
				this.opActivationFunction = ActivationFunctions::relu;
				this.opDerivativeFunction = DerivativeFunctions::reluDerivative;
				break;

			case BOOL_CLASSIFICATION:
			case SINGLE_CLASSIFICATION:
			case MULILABEL_CLASSIFICATION:
				this.hiddenActivationFunction = ActivationFunctions::sigmoid;
				this.hiddenDerivativeFunction = DerivativeFunctions::sigmoidDerivative;
				this.opActivationFunction = ActivationFunctions::sigmoid;
				this.opDerivativeFunction = DerivativeFunctions::sigmoidDerivative;
				break;

			default:
				break;
		}
	}

	// Store activations so that we can do backpropagation in training
	private double[] feedForward(double[] inputs, boolean storeLayers) {
		// Convert inputs to matrix
		Matrix curLayer = Matrix.fromArray(inputs);

		if (storeLayers) {
			layers[0] = curLayer;
		}

		// Loop through weights and hiddenLayers to generate a guess
		for (int i = 0; i < weights.length - 1; i++) {
			// OP = sig(W.I + B)
			curLayer = Matrix.multiply(weights[i], curLayer); // W.I
			curLayer = Matrix.add(curLayer, biases[i]); // + B
			curLayer = Matrix.mapElements(curLayer, this.hiddenActivationFunction); // activation(x)

			if (storeLayers) {
				layers[i + 1] = curLayer;
			}
		}

		int i = weights.length - 1;
		// OP = sig(W.I + B)
		curLayer = Matrix.multiply(weights[i], curLayer); // W.I
		curLayer = Matrix.add(curLayer, biases[i]); // + B
		curLayer = Matrix.mapElements(curLayer, this.opActivationFunction); // activation(x)

		if (storeLayers) {
			layers[i + 1] = curLayer;
		}

		// Return the prediction
		return curLayer.toArray();
	}

	public double[] feedForward(double[] inputs) {
		return feedForward(inputs, false);
	}

	private void backPropagation(double[] answers) {
		Matrix myOp = layers[layers.length - 1];
		Matrix ansMatrix = Matrix.fromArray(answers);

		// Calculate the errors
		Matrix opErrors = Matrix.subtract(ansMatrix, myOp);

		Matrix inpErrors = null;

		int i = weights.length - 1;
		// Generate errors of the input to this layer
		if (i != 0) {
			Matrix weightsTrans = Matrix.transpose(weights[i]);
			inpErrors = Matrix.multiply(weightsTrans, opErrors);
		}

		// Transpose of the inputs
		Matrix inpTrans = Matrix.transpose(layers[i]);

		// Derivative of the output of this layer / weights
		Matrix opDerivative = Matrix.mapElements(layers[i + 1], this.opDerivativeFunction);

		// biasDelta = lr * E * dx(O)
		Matrix biasDelta = Matrix.multiply(opErrors, this.learningRate);
		biasDelta = Matrix.multiplyElements(biasDelta, opDerivative);

		// weightsDelta = lr * E * dx(O) . inpTranspose
		Matrix weightsDelta = Matrix.multiply(biasDelta, inpTrans);

		// Add deltas to the weights and biases
		biases[i] = Matrix.add(biases[i], biasDelta);
		weights[i] = Matrix.add(weights[i], weightsDelta);

		// This layers inpErrors are previous layers opErrors
		opErrors = inpErrors;

		for (i = weights.length - 2; i >= 0; i--) {
			// Generate errors of the input to this layer
			if (i != 0) {
				Matrix weightsTrans = Matrix.transpose(weights[i]);
				inpErrors = Matrix.multiply(weightsTrans, opErrors);
			}

			// Transpose of the inputs
			inpTrans = Matrix.transpose(layers[i]);

			// Derivative of the output of this layer / weights
			opDerivative = Matrix.mapElements(layers[i + 1], this.hiddenDerivativeFunction);

			// biasDelta = lr * E * dx(O)
			biasDelta = Matrix.multiply(opErrors, this.learningRate);
			biasDelta = Matrix.multiplyElements(biasDelta, opDerivative);

			// weightsDelta = lr * E * dx(O) . inpTranspose
			weightsDelta = Matrix.multiply(biasDelta, inpTrans);

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
		double[] prediction = this.feedForward(inputs);
		switch (this.opType) {
			case MULILABEL_CLASSIFICATION:
			case SINGLE_CLASSIFICATION:
			case BOOL_CLASSIFICATION:
				for (int i = 0; i < prediction.length; i++) {
					prediction[i] = (prediction[i] < 0.5) ? 0 : 1;
				}
				break;
			default:
				break;
		}
		return prediction;
	}

	public double[][] predict(double[][] inputs) {
		double[][] predictions = new double[inputs.length][];
		for (int i = 0; i < inputs.length; i++) {
			predictions[i] = this.predict(inputs[i]);
		}
		return predictions;
	}

	public double test(double[][] inputs, double[][] outputs) {
		if (this.opType == OpType.REGRESSION) {
			double prediction;
			double err;
			double msme = 0;
			for (int i = 0; i < inputs.length; i++) {
				prediction = predict(inputs[i])[0];
				err = outputs[i][0] - prediction;
				msme += err * err;
			}
			return msme;
		} else if (this.opType == OpType.BOOL_CLASSIFICATION) {
			int correct = 0;
			double prediction;
			for (int i = 0; i < inputs.length; i++) {
				prediction = predict(inputs[i])[0];
				prediction = (prediction < 0.5) ? 0 : 1;
				if (prediction == outputs[i][0]) {
					correct++;
				}
			}
			return (correct * 1.0) / inputs.length;
		}
		return 0;
	}
}