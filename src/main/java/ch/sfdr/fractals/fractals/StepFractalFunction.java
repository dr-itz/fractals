package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Fractals based on step functions
 * @author S.Freihofer
 */
public interface StepFractalFunction
{
	/**
	 * Returns the lower bounds of the fractal function
	 * @return lower bounds as a complex number
	 */
	ComplexNumber getLowerBounds();

	/**
	 * Returns the upper bounds of the fractal function
	 * @return upper bounds as a complex number
	 */
	ComplexNumber getUpperBounds();

	/**
	 * Returns the square of the absolute value of the boundary
	 * @return square of boundary
	 */
	double getBoundarySqr();

	/**
	 * Calculates a single step
	 * @param start the starting number (z0)
	 * @param var initially the last number (z_n), updated by the function to be
	 * 		the next number z_{n+1}. Since this is mutable it is trickier to
	 * 		handle for certain operations.
	 */
	void step(ComplexNumber start, ComplexNumber var);
}
