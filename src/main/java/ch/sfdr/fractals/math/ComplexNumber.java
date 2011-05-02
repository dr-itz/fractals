package ch.sfdr.fractals.math;

/**
 * Represents an immutable complex number based on DoubleDouble values (~30 digits)
 * FIXME: it's minimal and incomplete: divide() and other ops are missing
 */
public class ComplexNumber
	implements Cloneable
{
	private double real;
	private double imaginary;

	/**
	 * creates a complex based on two double values
	 * @param real the real part
	 * @param imaginary the imaginary part
	 */
	public ComplexNumber(double real, double imaginary)
	{
		this.real = real;
		this.imaginary = imaginary;
	}

	/**
	 * adds another ComplexNumber and returns the result
	 * @param b the complex number to add
	 * @return result
	 */
	public ComplexNumber add(ComplexNumber b)
	{
		return new ComplexNumber(real + b.real, imaginary + b.imaginary);
	}

	/**
	 * subtracts another ComplexNumber and returns the result
	 * @param b the complex number to subtract
	 * @return result
	 */
	public ComplexNumber subtract(ComplexNumber b)
	{
		return new ComplexNumber(real - b.real, imaginary - b.imaginary);
	}

	/**
	 * multiplies by another complex.
	 * 	(a + bi)*(c + di) = (a*c - b*d) + (b*c + a*d)*i
	 * @param m the number to multiply with
	 * @return result
	 */
	public ComplexNumber multiply(ComplexNumber m)
	{
		double c = m.real;
		double d = m.imaginary;
		return new ComplexNumber(real * c - imaginary * d,
			real * d + imaginary * c);
	}

	/**
	 * multiplies by a DoubleDouble
	 * @param m the number to multiply with
	 * @return result
	 */
	public ComplexNumber multiply(double m)
	{
		return new ComplexNumber(real * m, imaginary * m);
	}

	/**
	 * returns the square of this complex number
	 * @return square
	 */
	public ComplexNumber square()
	{
		return multiply(this);
	}

	/**
	 * Returns the square of the absolute value of this complex
	 * @return absolute value
	 */
	public double absSqr()
	{
		return real * real + imaginary * imaginary;
	}

	/**
	 * Returns the absolute value of this complex
	 * @return absolute value
	 */
	public double abs()
	{
		return Math.sqrt(absSqr());
	}

	/**
	 * @return the real
	 */
	public double getReal()
	{
		return real;
	}

	/**
	 * @return the imaginary
	 */
	public double getImaginary()
	{
		return imaginary;
	}
}
