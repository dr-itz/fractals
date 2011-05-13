package ch.sfdr.fractals.math;

import static org.junit.Assert.*;

import java.awt.Rectangle;

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
		assertEquals(-2.6, me.scaleY(20), TOL);
	}

	@Test
	public void testZoomIn()
	{
		initWithGtHeight();
		Rectangle rect = new Rectangle(60, 50, 150, 100);
		me.zoomIn(rect);

		assertEquals(2.0, me.getZoom(), TOL);
		assertEquals(-1.6, me.scaleX(20), TOL);
		assertEquals(-0.8, me.scaleY(20), TOL);

		me.resetZoom();

		initHeightGtWidth();
		rect = new Rectangle(50, 60, 100, 150);
		me.zoomIn(rect);

		assertEquals(2.0, me.getZoom(), TOL);
		assertEquals(-0.8, me.scaleX(20), TOL);
		assertEquals(-1.6, me.scaleY(20), TOL);
	}
}
