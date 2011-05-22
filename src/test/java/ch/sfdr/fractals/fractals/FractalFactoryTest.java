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
		assertEquals(4, ret.length);
		assertEquals("Mandelbrot", ret[0]);
		assertEquals("Mandelbrot ^3", ret[1]);
		assertEquals("Julia", ret[2]);
		assertEquals("Julia ^3", ret[3]);
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
		assertTrue(FractalFactory.getFractalFunction("Julia")
			instanceof Julia);
		assertNull(FractalFactory.getFractalFunction("Something"));
	}
}
