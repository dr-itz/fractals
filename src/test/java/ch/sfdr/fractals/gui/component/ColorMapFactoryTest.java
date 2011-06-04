package ch.sfdr.fractals.gui.component;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for ColorMapFactory
 * @author D.Ritz
 */
public class ColorMapFactoryTest
{
	@Test
	public void testGetMap()
	{
		ColorMap cm = ColorMapFactory.getMap(0); // the "Grayscale" map
		assertNotNull(cm);
		assertEquals("Grayscale", cm.getName());
		assertEquals(49, cm.getNumColors());

	}

	@Test
	public void testGetNames()
	{
		String[] names = ColorMapFactory.getNames();
		assertEquals(4, names.length);
		assertEquals("Grayscale", names[0]);
		assertEquals("Blue", names[1]);
		assertEquals("Fire", names[2]);
		assertEquals("Binary", names[3]);
	}
}
