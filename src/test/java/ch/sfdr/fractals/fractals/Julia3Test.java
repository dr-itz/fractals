package ch.sfdr.fractals.fractals;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.sfdr.fractals.math.ComplexNumber;


/**
 * Test for Julia^3 fractal
 * @author S.Freihofer
 */
public class Julia3Test
{
	private static final double TOL = 1.0e-5;

	private Julia3 me = new Julia3();

	@Test
	public void testGetConstant()
	{
		ComplexNumber constant = me.getConstant();
		assertEquals(-0.4, constant.getReal(), TOL);
		assertEquals(0.6, constant.getImaginary(), TOL);
	}

	@Test
	public void testSetConstant()
	{
		me.setConstant(new ComplexNumber(-0.5, 0.7));
		ComplexNumber constant = me.getConstant();
		assertEquals(-0.5, constant.getReal(), TOL);
		assertEquals(0.7, constant.getImaginary(), TOL);
	}

	@Test
	public void testGetLowerBounds()
	{
		ComplexNumber lowerBounds = me.getLowerBounds();
		assertEquals(-2, lowerBounds.getReal(), TOL);
		assertEquals(-2, lowerBounds.getImaginary(), TOL);
	}

	@Test
	public void testGetUpperBounds()
	{
		ComplexNumber upperBounds = me.getUpperBounds();
		assertEquals(2, upperBounds.getReal(), TOL);
		assertEquals(2, upperBounds.getImaginary(), TOL);
	}

	@Test
	public void testGetBoundarySqr()
	{
		assertEquals(4, me.getBoundarySqr(), TOL);
	}

	@Test
	public void testStep()
	{
		ComplexNumber start = new ComplexNumber(1.0, 2.0);
		ComplexNumber var = new ComplexNumber(2.0, 3.0);
		me.step(start, var);
		assertEquals(-46.4, var.getReal(), TOL);
		assertEquals(9.6, var.getImaginary(), TOL);
	}
}
