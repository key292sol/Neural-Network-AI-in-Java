package ai;

public class DerivativeFunctions {

	// Derivative of the sigmoid activation function based on its output
	public static double sigmoidDerivative(double sigmoidOutput) {
		return sigmoidOutput * (1 - sigmoidOutput);
	}

	// Derivative of the hyperbolic tangent (tanh) activation function based on its
	// output
	public static double tanhDerivative(double tanhOutput) {
		return 1 - tanhOutput * tanhOutput;
	}

	// Derivative of the ReLU activation function based on its output
	public static double reluDerivative(double reluOutput) {
		return (reluOutput > 0) ? 1 : 0;
	}

	// Derivative of the Leaky ReLU activation function based on its output
	public static double leakyReLUDerivative(double output, double alpha) {
		return (output > 0) ? 1 : alpha;
	}

	// Derivative of the Parametric ReLU (PReLU) activation function based on its
	// output
	public static double parametricReLUDerivative(double output, double alpha) {
		return (output > 0) ? 1 : alpha;
	}

	// Derivative of the Exponential Linear Unit (ELU) activation function based on
	// its output
	public static double eluDerivative(double output, double alpha) {
		return (output > 0) ? 1 : alpha * Math.exp(output);
	}

	// Derivative of the Scaled Exponential Linear Unit (SELU) activation function
	// based on its output
	public static double seluDerivative(double output, double alpha, double lambda) {
		return (output > 0) ? lambda : lambda * alpha * Math.exp(output);
	}
}
