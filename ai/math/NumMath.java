package ai.math;

public class NumMath {

	public static double mapValue(double val, double minA, double maxA, double minB, double maxB) {
		double x = val;
		x -= minA;
		x /= maxA;
		x *= maxB;
		x += minA;
		return x;
	}
	
}
