package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * The Julia fractal function
 * @author D.Ritz
 */
public class Julia
	implements StepFractalFunctionWithConst
{
	private ComplexNumber constant;

	/**
	 * default Julia constructor with constant set to "-0.4+0.6i"
	 */
	public Julia()
	{
		constant = new ComplexNumber(-0.4, 0.6);
	}

	@Override
	public ComplexNumber getLowerBounds()
	{
		return new ComplexNumber(-2, -2);
	}

	@Override
	public ComplexNumber getUpperBounds()
	{
		return new ComplexNumber(2, 2);
	}

	@Override
	public double getBoundarySqr()
	{
		return 4;
	}

	@Override
	public ComplexNumber getConstant()
	{
		return constant.clone();
	}

	@Override
	public void setConstant(ComplexNumber constant)
	{
		this.constant = constant.clone();
	}

	@Override
	public void step(ComplexNumber start, ComplexNumber var)
	{
		var.square().add(constant);
	}
}
