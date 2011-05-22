package ch.sfdr.fractals.fractals;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Tests for ComplexOrbitCycleFinder
 * @author D.Ritz
 */
public class ComplexOrbitCycleFinderTest
	implements ComplexOrbitCycleListener
{
	private static final double TOL = 1e-4;

	private static final ComplexNumber[] expected = {
		new ComplexNumber(-1.754877,      5.79303189e-9),
		new ComplexNumber(-9.6416475e-8,  1.09477510e-7),
		new ComplexNumber(-0.1225611,     0.74486170),
		new ComplexNumber(-0.1225611,    -0.74486170),
	};

	private int solutions = 0;

	@Test
	public void testFindAllCycles()
		throws InterruptedException
	{
		Mandelbrot mb = new Mandelbrot();
		ComplexOrbitCycleFinder finder = new ComplexOrbitCycleFinder(this);
		finder.findAllCycles(mb, 3, 0);
		finder.waitForCompletion();

		assertEquals(4, solutions);
	}

	@Override
	public boolean cycleFound(ComplexNumber z, int length)
	{
		boolean ok = false;
		for (ComplexNumber c : expected) {
			if (Math.abs(c.getReal() - z.getReal()) < TOL &&
				Math.abs(c.getImaginary() - z.getImaginary()) < TOL)
			{
				ok = true;
				break;
			}
		}
		if (!ok)
			fail("unexpected value: " + z);

		solutions++;
		return true;
	}
}
