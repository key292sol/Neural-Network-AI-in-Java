package ai;

public class ActivationFunctions {

	// Sigmoid activation function
	public static double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	// Hyperbolic Tangent (tanh) activation function
	public static double tanh(double x) {
		return Math.tanh(x);
	}

	// Rectified Linear Unit (ReLU) activation function
	public static double relu(double x) {
		return Math.max(0, x);
	}

	// Leaky ReLU activation function
	public static double leakyReLU(double x, double alpha) {
		return (x > 0) ? x : alpha * x;
	}

	// Parametric ReLU (PReLU) activation function
	public static double parametricReLU(double x, double alpha) {
		return (x > 0) ? x : alpha * x;
	}

	// Exponential Linear Unit (ELU) activation function
	public static double elu(double x, double alpha) {
		return (x > 0) ? x : alpha * (Math.exp(x) - 1);
	}

	// Scaled Exponential Linear Unit (SELU) activation function
	public static double selu(double x, double alpha, double lambda) {
		return (x > 0) ? lambda * x : lambda * alpha * (Math.exp(x) - 1);
	}
}
