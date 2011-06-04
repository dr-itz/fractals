package ch.sfdr.fractals.gui.component;

import java.awt.Color;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Test for ColorMap
 * @author S.Freihofer
 */
public class ColorMapTest
{
	private Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

	ColorMap me = new ColorMap("RGB", colors);

	@Test
	public void testGetName()
	{
		assertEquals("RGB", me.getName());
	}

	@Test
	public void testGetColor()
	{
		assertEquals(colors[0], me.getColor(3));
		assertEquals(colors[1], me.getColor(4));
		assertEquals(colors[2], me.getColor(5));
	}
}
