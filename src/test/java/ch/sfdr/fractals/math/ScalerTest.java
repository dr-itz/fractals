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

	private void initWidthGtHeight()
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
		initWidthGtHeight();
		assertEquals(-2.6, me.scaleX(20), TOL);

		initHeightGtWidth();
		assertEquals(-1.6, me.scaleX(20), TOL);
	}

	@Test
	public void testScaleY()
	{
		initWidthGtHeight();
		assertEquals(1.6, me.scaleY(20), TOL);

		initHeightGtWidth();
		assertEquals(2.6, me.scaleY(20), TOL);
	}

	@Test
	public void testZoomIn()
	{
		initWidthGtHeight();
		Rectangle rect = new Rectangle(60, 50, 150, 100);
		me.zoomIn(rect);

		assertEquals(2.0, me.getZoom(), TOL);
		assertEquals(-1.6, me.scaleX(20), TOL);
		assertEquals(0.8, me.scaleY(20), TOL);

		me.resetZoom();

		initHeightGtWidth();
		rect = new Rectangle(50, 60, 100, 150);
		me.zoomIn(rect);

		assertEquals(2.0, me.getZoom(), TOL);
		assertEquals(-0.8, me.scaleX(20), TOL);
		assertEquals(1.6, me.scaleY(20), TOL);
	}

	@Test
	public void testZoomOut()
	{
		initWidthGtHeight();
		Rectangle rect = new Rectangle(60, 50, 75, 50);

		me.zoomIn(rect);
		assertEquals(4.0, me.getZoom(), TOL);

		me.zoomOut(50, 50, 2);
		assertEquals(2.0, me.getZoom(), TOL);
		assertEquals(-2.6, me.scaleX(20), TOL);
		assertEquals(1.55, me.scaleY(20), TOL);

		me.resetZoom();

		initHeightGtWidth();
		rect = new Rectangle(50, 60, 50, 75);
		me.zoomIn(rect);
		assertEquals(4.0, me.getZoom(), TOL);

		me.zoomOut(50, 50, 2);
		assertEquals(2.0, me.getZoom(), TOL);
		assertEquals(-1.55, me.scaleX(20), TOL);
		assertEquals(2.6, me.scaleY(20), TOL);

		me.zoomOut(50, 50, 3);
		assertEquals(1.0, me.getZoom(), TOL);
	}


	@Test
	public void testUnscaleX()
	{
		initWidthGtHeight();
		double scaledX = me.scaleX(20);
		assertEquals(20, me.unscaleX(scaledX), TOL);

		initHeightGtWidth();
		scaledX = me.scaleX(20);
		assertEquals(20, me.unscaleX(scaledX), TOL);
	}

	@Test
	public void testUnscaleY()
	{
		initWidthGtHeight();
		double scaledY = me.scaleY(20);
		assertEquals(20, me.unscaleY(scaledY), TOL);

		initHeightGtWidth();
		scaledY = me.scaleY(20);
		assertEquals(20, me.unscaleY(scaledY), TOL);
	}
}
