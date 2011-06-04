package ch.sfdr.fractals.gui.component;

import java.awt.Color;

/**
 * Maps iteration counts to a color
 * @author S.Freihofer
 */
public class ColorMap
{
	private String name;
	private Color[] colors;

	/**
	 * Constructor used by ColorMapFactory
	 * @param name the name of the ColorMap
	 * @param colors the different colors of the ColorMap
	 */
	public ColorMap(String name, Color[] colors)
	{
		this.name = name;
		this.colors = colors;
	}

	/**
	 * Gets the name of a ColorMap
	 * @return the name the name of a specific ColorMap
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * returns the number of colors in this map
	 * @return number of colors
	 */
	public int getNumColors()
	{
		return colors.length;
	}

	/**
	 * Maps the iteration count to a color
	 * @param iterations
	 * @return the mapped color
	 */
	public Color getColor(int iterations)
	{
		return colors[iterations % colors.length];
	}
}
