package ch.sfdr.fractals.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for LineClipping
 * @author D.Ritz
 */
public class LineClippingTest
{
	private LineClipping me;

	@Before
	public void setUp()
		throws Exception
	{
		me = new LineClipping();
	}

	@Test
	public void testIsPointInside()
	{
		assertTrue(LineClipping.isPointInside(10, 10, 0, 0, 100, 100));
		assertFalse(LineClipping.isPointInside(-5, 10, 0, 0, 100, 100));
		assertFalse(LineClipping.isPointInside(200, 10, 0, 0, 100, 100));
		assertFalse(LineClipping.isPointInside(5, -10, 0, 0, 100, 100));
		assertFalse(LineClipping.isPointInside(5, 200, 0, 0, 100, 100));
	}

	@Test
	public void testClipLineToRectangle()
	{
		// line completely inside
		assertTrue(me.clipLineToRectangle(5, 15, 10, 25, 0, 0, 100, 100));
		assertEquals(5, me.getClipX1());
		assertEquals(15, me.getClipY1());
		assertEquals(10, me.getClipX2());
		assertEquals(25, me.getClipY2());

		// line outside
		assertFalse(me.clipLineToRectangle(-10, -10, -5, -5, 0, 0, 100, 100));

		// line partially inside: LEFT
		assertTrue(me.clipLineToRectangle(-5, 15, 10, 25, 0, 0, 100, 100));
		assertEquals(0, me.getClipX1());
		assertEquals(18, me.getClipY1());
		assertEquals(10, me.getClipX2());
		assertEquals(25, me.getClipY2());

		assertTrue(me.clipLineToRectangle(10, 25, -5, 15, 0, 0, 100, 100));
		assertEquals(10, me.getClipX1());
		assertEquals(25, me.getClipY1());
		assertEquals(0, me.getClipX2());
		assertEquals(18, me.getClipY2());

		// line partially inside: TOP
		assertTrue(me.clipLineToRectangle(50, 200, 10, 20, 0, 0, 100, 100));
		assertEquals(28, me.getClipX1());
		assertEquals(100, me.getClipY1());
		assertEquals(10, me.getClipX2());
		assertEquals(20, me.getClipY2());

		// line partially inside: BOTTOM
		assertTrue(me.clipLineToRectangle(50, -200, 10, 20, 0, 0, 100, 100));
		assertEquals(14, me.getClipX1());
		assertEquals(0, me.getClipY1());
		assertEquals(10, me.getClipX2());
		assertEquals(20, me.getClipY2());

		// line partially inside: RIGHT
		assertTrue(me.clipLineToRectangle(150, 15, 10, 25, 0, 0, 100, 100));
		assertEquals(100, me.getClipX1());
		assertEquals(19, me.getClipY1());
		assertEquals(10, me.getClipX2());
		assertEquals(25, me.getClipY2());
	}
}
