package ch.sfdr.fractals.gui.component;

import java.awt.Color;

/**
 * Selection for different colors
 * @author S.Freihofer
 */
public class ColorSelection
{
	private static final Object[] colors = {
		"Red",		Color.RED,
		"Green",	Color.GREEN,
		"Blue",		Color.BLUE,
		"Yellow",	Color.YELLOW,
		"Cyan",		Color.CYAN,
		"Magenta",	new Color(240, 0, 255),
		"Orange",	new Color(255, 130, 0),
		"Pink",		new Color(255, 68, 187),
		"Black",	Color.BLACK,
	};

	/**
	 * Returns a Color by index
	 * @param idx the index of a Color
	 * @return Color
	 */
	public static Color getColor(int idx)
	{
		return (Color) colors[idx * 2 + 1];
	}

	/**
	 * Returns a list of names
	 * @return String[] with the specific names of a Color
	 */
	public static String[] getNames()
	{
		String[] names = new String[colors.length / 2];
		for (int i = 0; i < colors.length / 2; i++) {
			names[i] = (String) colors[2 * i];
		}
		return names;
	}
}
