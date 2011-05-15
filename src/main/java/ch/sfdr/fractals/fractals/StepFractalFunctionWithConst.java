package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Step function with constant (eg. for Julia)
 * @author D.Ritz
 */
public interface StepFractalFunctionWithConst
	extends StepFractalFunction
{
	/**
	 * returns the constant currently set
	 * @return ComplexNumber constant
	 */
	ComplexNumber getConstant();

	/**
	 * sets the constant used in calculations
	 * @param constant the constant as ComplexNumber
	 */
	void setConstant(ComplexNumber constant);
}
