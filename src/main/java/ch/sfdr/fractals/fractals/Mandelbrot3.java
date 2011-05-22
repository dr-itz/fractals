package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Mandelbrot^3 fractal
 * @author S.Freihofer
 */
public class Mandelbrot3
	implements StepFractalFunction
{
	/*
	 * @see ch.sfdr.fractals.fractals.StepFractalFunction#getLowerBounds()
	 */
	@Override
	public ComplexNumber getLowerBounds()
	{
		return new ComplexNumber(-2, -2);
	}

	/*
	 * @see ch.sfdr.fractals.fractals.StepFractalFunction#getUpperBounds()
	 */
	@Override
	public ComplexNumber getUpperBounds()
	{
		return new ComplexNumber(2, 2);
	}

	/*
	 * @see ch.sfdr.fractals.fractals.StepFractalFunction#getBoundarySqr()
	 */
	@Override
	public double getBoundarySqr()
	{
		return 4;
	}

	/*
	 * @see
	 * ch.sfdr.fractals.fractals.StepFractalFunction#step(ch.sfdr.fractals.math
	 * .ComplexNumber, ch.sfdr.fractals.math.ComplexNumber)
	 */
	@Override
	public void step(ComplexNumber start, ComplexNumber var)
	{
		var.pow(3).add(start);
	}
}
