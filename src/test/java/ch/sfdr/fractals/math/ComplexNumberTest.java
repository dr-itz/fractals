package ch.sfdr.fractals.math;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for ComplexNumber
 */
public class ComplexNumberTest
{
	private static final double TOL = 1.0e-10;

	@Test
	public void testClone()
	{
		ComplexNumber x = new ComplexNumber(1.0, 2.0);
		ComplexNumber z = x.clone();
		assertEquals(1.0, z.getReal(), TOL);
		assertEquals(2.0, z.getImaginary(), TOL);
	}

	@Test
	public void testAdd()
	{
		ComplexNumber x = new ComplexNumber(1.0, 2.0);
		ComplexNumber y = new ComplexNumber(2.0, 3.0);
		ComplexNumber z = x.add(y);
		assertEquals(3.0, z.getReal(), TOL);
		assertEquals(5.0, z.getImaginary(), TOL);
	}

	@Test
	public void testSubtract()
	{
		ComplexNumber x = new ComplexNumber(1.0, 2.0);
		ComplexNumber y = new ComplexNumber(2.0, 4.0);
		ComplexNumber z = x.subtract(y);
		assertEquals(-1.0, z.getReal(), TOL);
		assertEquals(-2.0, z.getImaginary(), TOL);
	}

	@Test
	public void testMultiplyComplexNumber()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		ComplexNumber y = new ComplexNumber(5.0, 6.0);
		ComplexNumber z = x.multiply(y);
		assertEquals(-9.0, z.getReal(), TOL);
		assertEquals(38.0, z.getImaginary(), TOL);
	}

	@Test
	public void testMultiplyDouble()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		ComplexNumber z = x.multiply(2.0D);
		assertEquals(6.0, z.getReal(), TOL);
		assertEquals(8.0, z.getImaginary(), TOL);
	}

	@Test
	public void testDivide()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		ComplexNumber y = new ComplexNumber(5.0, 6.0);
		ComplexNumber z = x.divide(y);
        assertEquals(39.0 / 61.0, z.getReal(), TOL);
        assertEquals(2.0 / 61.0, z.getImaginary(), TOL);
    }


	@Test
	public void testSquare()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		ComplexNumber z = x.square();
		assertEquals(-7.0, z.getReal(), TOL);
		assertEquals(24.0, z.getImaginary(), TOL);
	}

	@Test
	public void testPow()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		x.pow(3);
		assertEquals(-117.0, x.getReal(), TOL);
		assertEquals(44.0, x.getImaginary(), TOL);
	}

	@Test
	public void testAbsSqr()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		double z = x.absSqr();
		assertEquals(25.0, z, TOL);
	}

	@Test
	public void testAbs()
	{
		ComplexNumber x = new ComplexNumber(3.0, 4.0);
		double z = x.abs();
		assertEquals(5.0, z, TOL);
	}
}
