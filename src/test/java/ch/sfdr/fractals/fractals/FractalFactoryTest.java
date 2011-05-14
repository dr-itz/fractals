package ch.sfdr.fractals.fractals;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for FractalFactory
 * @author S.Freihofer
 */
public class FractalFactoryTest
{
	@Test
	public void testGetFractalFunctionsNames()
	{
		String[] ret = FractalFactory.getFractalFunctionsNames();
		assertEquals(1, ret.length);
		assertEquals("Mandelbrot", ret[0]);
	}

	@Test
	public void testGetFractalFunction()
	{
		assertTrue(FractalFactory.getFractalFunction(0) instanceof Mandelbrot);
		assertNull(FractalFactory.getFractalFunction(5));
	}

	@Test
	public void testGetFractalFunctionByName()
	{
		assertTrue(FractalFactory.getFractalFunction("Mandelbrot")
			instanceof Mandelbrot);
		assertNull(FractalFactory.getFractalFunction("Something"));
	}
}