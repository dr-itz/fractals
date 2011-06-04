package ch.sfdr.fractals.gui.component;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * Tests for ColorSelection
 * @author D.Ritz
 */
public class ColorSelectionTest
{
	@Test
	public void testGetColor()
	{
		assertEquals(Color.RED, ColorSelection.getColor(0));
		assertEquals(Color.GREEN, ColorSelection.getColor(1));
		assertEquals(Color.BLUE, ColorSelection.getColor(2));
		assertEquals(Color.YELLOW, ColorSelection.getColor(3));
		assertEquals(Color.CYAN, ColorSelection.getColor(4));
		assertEquals(Color.BLACK, ColorSelection.getColor(8));
	}

	@Test
	public void testGetNames()
	{
		String[] names = ColorSelection.getNames();
		assertEquals(9, names.length);
		assertEquals("Red", names[0]);
		assertEquals("Green", names[1]);
		assertEquals("Blue", names[2]);
		assertEquals("Yellow", names[3]);
		assertEquals("Cyan", names[4]);
		assertEquals("Magenta", names[5]);
		assertEquals("Orange", names[6]);
		assertEquals("Pink", names[7]);
		assertEquals("Black", names[8]);
	}
}
