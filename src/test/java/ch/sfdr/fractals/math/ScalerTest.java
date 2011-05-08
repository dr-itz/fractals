package ch.sfdr.fractals.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for scaler
 * @author D. Ritz
 */
public class ScalerTest
{
	private static final double TOL = 10e-10;
	private Scaler me;

	@Before
	public void setUp()
		throws Exception
	{
		me = new Scaler();
		me.init(-2D, -2D, 2D, 2D);
	}

	private void initWithGtHeight()
	{
		me.setDimension(300, 200);
	}

	private void initHeightGtWidth()
	{
		me.setDimension(200, 300);
	}

	@Test
	public void testScaleX()
	{
		initWithGtHeight();
		assertEquals(-2.6, me.scaleX(20), TOL);

		initHeightGtWidth();
		assertEquals(-1.6, me.scaleX(20), TOL);
	}

	@Test
	public void testScaleY()
	{
		initWithGtHeight();
		assertEquals(-1.6, me.scaleY(20), TOL);

		initHeightGtWidth();
		assertEquals(-0.6, me.scaleY(20), TOL);
	}
}
