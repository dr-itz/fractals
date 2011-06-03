package ch.sfdr.fractals.fractals;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.sfdr.fractals.math.ComplexNumber;

/**
 * Tests for ComplexOrbitCycleFinder
 * @author D.Ritz
 */
public class ComplexOrbitCycleFinderTest
	implements ComplexOrbitCycleListener, StatisticsObserver
{
	private static final double TOL = 1e-4;

	private static final ComplexNumber[] expected = {
		new ComplexNumber(-1.754877,      5.79303189e-9),
		new ComplexNumber(-9.6416475e-8,  1.09477510e-7),
		new ComplexNumber(-0.1225611,     0.74486170),
		new ComplexNumber(-0.1225611,    -0.74486170),
	};

	private ComplexOrbitCycleFinder finder;
	private Mandelbrot mandelbrot;

	private int solutions = 0;
	private boolean statDataAvail = false;

	@Before
	public void setUp()
	{
		mandelbrot = new Mandelbrot();
		finder = new ComplexOrbitCycleFinder(this);
		finder.setStatObserver(this);
		assertEquals(this, finder.getStatObserver());
	}

	@Test
	public void testGetSetIterations()
	{
		assertEquals(400, finder.getMaxIter());
		finder.setMaxIter(500);
		assertEquals(500, finder.getMaxIter());
	}

	@Test
	public void testFindAllCycles()
		throws InterruptedException
	{
		finder.findAllCycles(mandelbrot, 3, 250);
		finder.waitForCompletion();

		assertEquals(4, solutions);
		assertEquals(4, finder.getCyclesFound());
		assertEquals(3, finder.getCycleLength());

		assertTrue(statDataAvail);
	}

	@Test
	public void testStop()
	{
		long start = System.currentTimeMillis();
		finder.findAllCycles(mandelbrot, 3, 1000);
		finder.stop();
		long dur = System.currentTimeMillis() - start;
		assertTrue(dur < 200);
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

	@Override
	public void statisticsDataAvailable(Object source)
	{
		assertEquals(finder, source);
		statDataAvail = true;
	}

	@Override
	public void updateProgess(Object source, int percent)
	{
		assertEquals(finder, source);
	}
}
