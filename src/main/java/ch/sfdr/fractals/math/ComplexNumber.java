package ch.sfdr.fractals.math;


/**
 * Represents an mutable(!) complex number based on double values (~15 digits)
 * @author D.Ritz
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

	/*
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ComplexNumber clone()
	{
		try {
			return (ComplexNumber) super.clone();
		} catch (CloneNotSupportedException e) {
			return null; // never reached
		}
	}

	/**
	 * sets the current complex number to new real/imaginary values
	 * @param real the real part
	 * @param imaginary the imaginary part
	 * @return ComplexNumber
	 */
	public ComplexNumber set(double real, double imaginary)
	{
		this.real = real;
		this.imaginary = imaginary;
		return this;
	}

	/**
	 * sets the current complex number to the value of another complex number
	 * @param other the other complex number
	 * @return reference to this
	 */
	public ComplexNumber set(ComplexNumber other)
	{
		return set(other.real, other.imaginary);
	}


	/**
	 * adds another ComplexNumber and returns the result
	 * @param b the complex number to add
	 * @return result
	 */
	public ComplexNumber add(ComplexNumber b)
	{
		return set(real + b.real, imaginary + b.imaginary);
	}

	/**
	 * subtracts another ComplexNumber and returns the result
	 * @param b the complex number to subtract
	 * @return result
	 */
	public ComplexNumber subtract(ComplexNumber b)
	{
		return set(real - b.real, imaginary - b.imaginary);
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
		return set(real * c - imaginary * d,
			real * d + imaginary * c);
	}

	/**
	 * multiplies by a DoubleDouble
	 * @param m the number to multiply with
	 * @return result
	 */
	public ComplexNumber multiply(double m)
	{
		return set(real * m, imaginary * m);
	}

	/**
	 * divides by another complex
	 * @param div the divisor
	 * @return result
	 */
	public ComplexNumber divide(ComplexNumber div)
	{
		double c = div.real;
		double d = div.imaginary;

		double q = d / c;
		double denom = d * q + c;
		return set((imaginary * q + real) / denom,
			(imaginary - real * q) / denom);
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
	 * Raises this complex number to the power of n. This implementation uses
	 * a series of multiplications, don't use for large n.
	 * @param n
	 * @return this
	 */
	public ComplexNumber pow(int n)
	{
		double c = real;
		double d = imaginary;

		for (int i = 1; i < n; i++) {
			double re = real * c - imaginary * d;
			double im = real * d + imaginary * c;
			real = re;
			imaginary = im;
		}
		return this;
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

	/**
	 * returns true if this complex is NotANumber
	 * @return NaN
	 */
	public boolean isNaN()
	{
		return Double.isNaN(real) || Double.isNaN(imaginary);
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(real);
		if (imaginary >= 0.0)
			sb.append("+");
		sb.append(imaginary).append("i");
		return sb.toString();
	}
}
