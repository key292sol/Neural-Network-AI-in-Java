package ai.math;

import java.util.Arrays;
import java.util.function.DoubleFunction;

public class Matrix {
	public int rows, cols;
	public double[][] matrix;

	/***************
	 * CONSTRUCTORS
	 ***************/

	public Matrix(int _rows, int _cols, boolean randomize) {
		this(new double[_rows][_cols]);

		if (randomize) {
			this.randomize();
		}
	}

	public Matrix(int _rows, int _cols) {
		this(_rows, _cols, true);
	}

	// This constructor sets the passed array as the matrix
	public Matrix(double[][] arr) {
		this.rows = arr.length;
		this.cols = arr[0].length;
		this.matrix = arr;
	}

	/*****************
	 * OBJECT METHODS
	 *****************/

	public void randomize(float a, float b) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				double random = Math.random() - 0.5;
				// matrix[i][j] = NumMath.mapValue(random, 0, 1, a, b);
				matrix[i][j] = random;
			}
		}
	}

	public void randomize() {
		this.randomize(-0.5f, 0.5f);
	}

	public double[] toArray() {
		double[] arr = new double[rows * cols];
		int i = 0;
		for (double[] ds : this.matrix) {
			for (double d : ds) {
				arr[i++] = d;
			}
		}
		return arr;
	}

	public String toString() {
		String s = "";
		for (double[] row : this.matrix) {
			s += Arrays.toString(row);
		}
		s += '\n';
		return s;
	}

	/*****************
	 * STATIC METHODS
	 *****************/

	// Converts the array cols to rows
	public static Matrix fromArray(double[] arr) {
		Matrix m = new Matrix(arr.length, 1);
		for (int i = 0; i < arr.length; i++) {
			m.matrix[i][0] = arr[i];
		}
		return m;
	}

	public static double[][] mapElements(double[][] m, DoubleFunction<Double> df) {
		int r = m.length, c = m[0].length;
		double[][] m2 = new double[r][c];

		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				m2[i][j] = df.apply(m[i][j]);
			}
		}
		return m2;
	}

	public static Matrix mapElements(Matrix m, DoubleFunction<Double> df) {
		double[][] d = Matrix.mapElements(m.matrix, df);
		return (new Matrix(d));
	}

	/**********************
	 * STATIC MATH METHODS
	 **********************/

	public static double[][] add(double[][] m1, double[][] m2) {
		int r1 = m1.length,
				c1 = m1[0].length,
				r2 = m2.length,
				c2 = m2[0].length;

		if ((r1 != r2) || (c1 != c2)) {
			System.err.println("The Dimensions of Matrix A does not match that of Matrix B");
			System.err.println("Rows: " + r1 + " " + r2 + "\nCols: " + c1 + " " + c2);
			System.out.println("Matrix.add()");
			return null;
		}

		double[][] res = new double[r1][c1];
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < c1; j++) {
				res[i][j] = m1[i][j] + m2[i][j];
			}
		}

		return res;
	}

	public static double[][] subtract(double[][] m1, double[][] m2) {
		int r1 = m1.length,
				c1 = m1[0].length,
				r2 = m2.length,
				c2 = m2[0].length;

		if ((r1 != r2) || (c1 != c2)) {
			System.err.println("The Dimensions of Matrix A does not match that of Matrix B");
			System.out.println("Matrix.subtract()");
			return null;
		}

		double[][] res = new double[r1][c1];
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < c1; j++) {
				res[i][j] = m1[i][j] - m2[i][j];
			}
		}

		return res;
	}

	public static double[][] multiply(double[][] m1, double[][] m2) {
		int r1 = m1.length,
				c1 = m1[0].length,
				r2 = m2.length,
				c2 = m2[0].length;

		if (c1 != r2) {
			System.err.println("The Columns of Matrix A does not match rows of Matrix B");
			System.out.println("Matrix.multiply()");
			return null;
		}

		double[][] newGrid = new double[r1][c2];

		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < c2; j++) {
				double sum = 0;
				for (int k = 0; k < c1; k++) {
					sum += m1[i][k] * m2[k][j];
				}
				newGrid[i][j] = sum;
			}
		}

		return newGrid;
	}

	public static double[][] multiply(double[][] m1, double num) {
		int r = m1.length, c = m1[0].length;
		double[][] m = new double[r][c];
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				m[i][j] = m1[i][j] * num;
			}
		}
		return m;
	}

	public static double[][] multiplyElements(double[][] m1, double[][] m2) {
		int r1 = m1.length,
				c1 = m1[0].length,
				r2 = m2.length,
				c2 = m2[0].length;

		if ((r1 != r2) || (c1 != c2)) {
			System.err.println("The Dimensions of Matrix A does not match that of Matrix B");
			System.out.println("Matrix.multiplyElements()");
			return null;
		}

		double[][] res = new double[r1][c1];
		for (int i = 0; i < r1; i++) {
			for (int j = 0; j < c1; j++) {
				res[i][j] = m1[i][j] * m2[i][j];
			}
		}

		return res;
	}

	public static double[][] transpose(double[][] m1) {
		int r = m1.length, c = m1[0].length;
		double[][] t = new double[c][r];
		for (int i = 0; i < r; i++) {
			for (int j = 0; j < c; j++) {
				t[j][i] = m1[i][j];
			}
		}
		return t;
	}

	public static Matrix add(Matrix m1, Matrix m2) {
		double[][] m = Matrix.add(m1.matrix, m2.matrix);
		return (new Matrix(m));
	}

	public static Matrix subtract(Matrix m1, Matrix m2) {
		double[][] m = Matrix.subtract(m1.matrix, m2.matrix);
		return (new Matrix(m));
	}

	public static Matrix multiply(Matrix m1, Matrix m2) {
		double[][] m = Matrix.multiply(m1.matrix, m2.matrix);
		return (new Matrix(m));
	}

	public static Matrix multiply(Matrix m1, double num) {
		double[][] m = Matrix.multiply(m1.matrix, num);
		return (new Matrix(m));
	}

	public static Matrix multiplyElements(Matrix m1, Matrix m2) {
		double[][] m = Matrix.multiplyElements(m1.matrix, m2.matrix);
		return (new Matrix(m));
	}

	public static Matrix transpose(Matrix m) {
		double[][] t = Matrix.transpose(m.matrix);
		return (new Matrix(t));
	}

}