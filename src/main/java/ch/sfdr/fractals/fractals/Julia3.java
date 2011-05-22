package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Julia^3 fractal
 * @author S.Freihofer
 */
public class Julia3
	implements StepFractalFunctionWithConst
{
	private ComplexNumber constant;

	/**
	 * default Julia3 constructor with constant set to "-0.4+0.6i"
	 */
	public Julia3()
	{
		constant = new ComplexNumber(-0.4, 0.6);
	}

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
		var.pow(3).add(constant);
	}

	/*
	 * @see ch.sfdr.fractals.fractals.StepFractalFunctionWithConst#getConstant()
	 */
	@Override
	public ComplexNumber getConstant()
	{
		return constant.clone();
	}

	/*
	 * @see
	 * ch.sfdr.fractals.fractals.StepFractalFunctionWithConst#setConstant(ch
	 * .sfdr.fractals.math.ComplexNumber)
	 */
	@Override
	public void setConstant(ComplexNumber constant)
	{
		this.constant = constant.clone();
	}
}
