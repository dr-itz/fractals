package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Finds cycles in orbits of escape time fractal functions.
 * Uses a Newton iterator with a Jacobi matrix to find the root for a given
 * start point
 * @author D.Ritz
 */
public class ComplexOrbitCycleFinder
{
	private static final double TOL = 1e-13;
	private static final int MAX_ITER = 400;

	private StepFractalFunction function;
	private int cycleLength;

	/**
	 * constructs a cycle finder for the given fractal function
	 */
	public ComplexOrbitCycleFinder(StepFractalFunction function)
	{
		this.function = function;
	}

	/**
	 * finds a cycle of given length, starting the Newton iteration with the
	 * given start value
	 * @param cycleLenght length of the cycle to find
	 * @param start the start value for the Newton iteration
	 * @return One possible point as ComplexNumber where a cycle occurs
	 */
	public ComplexNumber findCycle(int cycleLenght, ComplexNumber start)
	{
		this.cycleLength = cycleLenght;

		ComplexNumber z = start.clone();
		ComplexNumber f = callFunction(z);

		int i = 0;
		while ((Math.abs(f.getReal()) > TOL ||
				Math.abs(f.getImaginary()) > TOL) &&
				i++ < MAX_ITER)
		{
			// z = z - jacobian(z)^-1 * callFunction(z, cycleCount)

			// get the inverse of the jacobian
			Matrix2x2 jacInv = jacobian(z).invert();

			// get the function value
			f = callFunction(z);

			// multiply inverse of the jacobian with the current function value
			double re = jacInv.get(0, 0) * f.getReal() +
				jacInv.get(0, 1) * f.getImaginary();
			double im = jacInv.get(1, 0) * f.getReal() +
				jacInv.get(1, 1) * f.getImaginary();

			z.subtract(new ComplexNumber(re, im));
		}

		if (i >= MAX_ITER)
			return null;

		System.out.println("Iterations: " + i + "; z: " + z);
		return z;
	}

	/**
	 * calls the fractal function cycleLength times
	 * @param start the start value
	 * @return the value after cycleCount iterations
	 */
	private ComplexNumber callFunction(ComplexNumber start)
	{
		ComplexNumber z = start.clone();
		for (int i = 0; i < cycleLength; i++)
			function.step(start, z);
		// bring it to the f(z) = 0 form
		z = z.subtract(start);
		return z;
	}

	/**
	 * builds the jacobian matrix with numerical differentiation (central diff')
	 * @param z
	 * @return the jacobian Matrix2x2
	 */
	private Matrix2x2 jacobian(ComplexNumber z)
	{
		Matrix2x2 ret = new Matrix2x2();

		// smaller value will lead to NaN problems
		double h = 1e-8;

		// x part:
		ComplexNumber dx = new ComplexNumber(h, 0);

		ComplexNumber zdx = z.clone();
		zdx.add(dx);
		zdx = callFunction(zdx);

		ComplexNumber zdx2 = z.clone();
		zdx2.subtract(dx);
		zdx2 = callFunction(zdx2);

		zdx.subtract(zdx2);
		ret.set(0, 0, zdx.getReal() / (2*h));
		ret.set(0, 1, zdx.getImaginary() / (2*h));

		// same again, but for y part
		ComplexNumber dy = new ComplexNumber(0, h);

		ComplexNumber zdy = z.clone();
		zdy.add(dy);
		zdy = callFunction(zdy);

		ComplexNumber zdy2 = z.clone();
		zdy2.subtract(dy);
		zdy2 = callFunction(zdy2);

		zdy.subtract(zdy2);
		ret.set(1, 0, zdy.getReal() / (2*h));
		ret.set(1, 1, zdy.getImaginary() / (2*h));

		return ret;
	}


	/**
	 * A 2x2 matrix, with the ability to invert itself
	 */
	private static class Matrix2x2
	{
		private double[][] elem = new double[2][2];

		public double get(int x, int y)
		{
			return elem[x][y];
		}

		public void set(int x, int y, double val)
		{
			elem[x][y] = val;
		}

		public Matrix2x2 invert()
		{
			double det = elem[0][0] * elem[1][1] -
				elem[0][1] * elem[1][0];

			double[][] inv = new double[2][2];
			inv[0][0] =  elem[1][1] / det;
			inv[0][1] = -elem[0][1] / det;
			inv[1][0] = -elem[1][0] / det;
			inv[1][1] =  elem[0][0] / det;

			elem = inv;

			return this;
		}
	}
}
