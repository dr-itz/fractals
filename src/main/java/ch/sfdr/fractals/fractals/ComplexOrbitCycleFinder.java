package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Finds cycles in orbits of escape time fractal functions.
 * Uses a Newton iterator with a Jacobi matrix to find the root for a given
 * start point. The basic iteration:
 *   z = z - jacobian(z)^{-1} * f(z)
 * @author D.Ritz
 */
public class ComplexOrbitCycleFinder
{
	// the tolerance
	private static final double TOL = 1e-13;

	// the "h" used in diff. Smaller values would case NaN problems
	private static final double DIFF_H = 1e-8;

	private StepFractalFunction function;
	private int cycleLength;

	// the maximum number of iterations
	private int maxIter = 400;

	/**
	 * constructs a cycle finder for the given fractal function
	 */
	public ComplexOrbitCycleFinder(StepFractalFunction function)
	{
		this.function = function;
	}

	/**
	 * returns the maximum number of iterations allowed
	 * @return the max number of iterations
	 */
	public int getMaxIter()
	{
		return maxIter;
	}

	/**
	 * sets the maximum number of iterations allowed
	 * @param maxIter max number of iterations
	 */
	public void setMaxIter(int maxIter)
	{
		this.maxIter = maxIter;
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
		long startTime = System.currentTimeMillis();

		this.cycleLength = cycleLenght;

		// the value that should converge to the solution
		ComplexNumber z = start.clone();

		// the function value, i.e. the difference to 0
		ComplexNumber f = new ComplexNumber(1, 1);

		// the delta from the jacobian multiplied by the function value
		ComplexNumber delta = new ComplexNumber(0, 0);

		// the jacobian (and it's inverse)
		Jacobian2x2 jac = new Jacobian2x2();

		int i = 0;
		while ((Math.abs(f.getReal()) > TOL ||
				Math.abs(f.getImaginary()) > TOL) &&
				i++ < maxIter)
		{
			// get the inverse of the jacobian
			jac.jacobian(z).invert();

			// get the function value
			callFunction(z, f);

			// multiply inverse of the jacobian with the current function value
			delta.set(
				jac.get(0, 0) * f.getReal() +
					jac.get(0, 1) * f.getImaginary(),
				jac.get(1, 0) * f.getReal() +
					jac.get(1, 1) * f.getImaginary());

			// subtract the delta form the current value
			z.subtract(delta);
		}

		long dur = System.currentTimeMillis() - startTime;
		System.out.println("Duration: " + dur + "ms; Iterations: " + i + "; z: " + z);

		if (i >= maxIter || z.isNaN())
			return null;

		return z;
	}

	/**
	 * calls the fractal function cycleLength times
	 * @param start the start value
	 * @param res the result
	 * @return the value after cycleCount iterations
	 */
	private ComplexNumber callFunction(ComplexNumber start, ComplexNumber res)
	{
		res.set(start);
		for (int i = 0; i < cycleLength; i++)
			function.step(start, res);
		// bring it to the f(z) = 0 form
		res = res.subtract(start);
		return res;
	}

	/**
	 * A 2x2 matrix jacobian, with the ability to invert itself.
	 * All objects are pre-allocated.
	 */
	private class Jacobian2x2
	{
		private double[][] elem = new double[2][2];
		private double[][] inv = new double[2][2];
		private ComplexNumber start = new ComplexNumber(0, 0);
		private ComplexNumber zd = new ComplexNumber(0, 0);
		private ComplexNumber zd2 = new ComplexNumber(0, 0);

		public double get(int x, int y)
		{
			return elem[x][y];
		}

		public void set(int x, int y, double val)
		{
			elem[x][y] = val;
		}

		/**
		 * inverts this jacobian
		 * @return this
		 */
		public Jacobian2x2 invert()
		{
			double det = elem[0][0] * elem[1][1] -
				elem[0][1] * elem[1][0];

			inv[0][0] =  elem[1][1] / det;
			inv[0][1] = -elem[0][1] / det;
			inv[1][0] = -elem[1][0] / det;
			inv[1][1] =  elem[0][0] / det;

			// just swap the two buffers - no instantiations
			double[][] tmp = elem;
			elem = inv;
			inv = tmp;

			return this;
		}

		/**
		 * builds the jacobian matrix with numerical differentiation (central diff')
		 * @param z the complex number
		 * @return this
		 */
		public Jacobian2x2 jacobian(ComplexNumber z)
		{
			// x part
			start.set(z.getReal() + DIFF_H, z.getImaginary());
			callFunction(start, zd);

			start.set(z.getReal() - DIFF_H, z.getImaginary());
			callFunction(start, zd2);

			zd.subtract(zd2);
			set(0, 0, zd.getReal() / (2 * DIFF_H));
			set(1, 0, zd.getImaginary() / (2 * DIFF_H));

			// y part:
			start.set(z.getReal(), z.getImaginary() + DIFF_H);
			callFunction(start, zd);

			start.set(z.getReal(), z.getImaginary() - DIFF_H);
			callFunction(start, zd2);
			zd.subtract(zd2);
			set(0, 1, zd.getReal() / (2 * DIFF_H));
			set(1, 1, zd.getImaginary() / (2 * DIFF_H));

			return this;
		}
	}
}
