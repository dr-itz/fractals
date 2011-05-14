package ch.sfdr.fractals.gui.component;

import java.awt.Color;

/**
 * Factory for ColorMaps based on color values interpolated from some reference
 * colors defined here.
 * @author S.Freihofer
 */
public class ColorMapFactory
{
	/**
	 * Reference colors: { Number of steps, R, B, G }
	 */
	private static final ColorMapDefinition[] DEFINITIONS = {
		defineMap("Grayscale", new int[][] {
			{16, 0, 0, 0}, {32, 255, 255, 255}
		}),

		defineMap("Blue", new int[][] {
			{12, 0, 0, 64}, {12, 0, 0, 255}, {10, 0, 255, 255},
			{12, 128, 255, 255}, {14, 64, 128, 255}
		}),

		defineMap("Fire", new int[][] {
			{10, 70, 0, 20}, {10, 100, 0, 100}, {14, 255, 0, 0},
			{10, 255, 200, 0}
		}),

		defineMap("Binary", new int[][] {
			{1, 255, 255, 255}
		}),
	};

	private static ColorMap[] maps;

	static {
		maps = new ColorMap[DEFINITIONS.length];

		int map = 0;
		for (ColorMapDefinition def : DEFINITIONS) {
			int n = 0;
			// get the number of all colors
			for (int i = 0; i < def.colorValues.length; i++)
				n += def.colorValues[i][0];

			Color[] colors = new Color[n];
			n = 0;
			// interpolate all colors
			for (int i = 0; i < def.colorValues.length; i++) {
				// first referential color
				int[] c1 = def.colorValues[i];
				// second ref. color
				int[] c2 = def.colorValues[(i + 1) % def.colorValues.length];
				for (int j = 0; j < c1[0]; j++)
					// linear interpolation of RGB values
					colors[n + j] = new Color(
						(c1[1] * (c1[0] - j) + c2[1] * j) / c1[0],
						(c1[2] * (c1[0] - j) + c2[2] * j) / c1[0],
						(c1[3] * (c1[0] - j) + c2[3] * j) / c1[0]);
				n += c1[0];
			}

			maps[map++] = new ColorMap(def.name, colors);;
		}
	}

	/**
	 * Returns a ColorMap by index
	 * @param idx the index of a ColorMap
	 * @return ColorMap
	 */
	public static ColorMap getMap(int idx)
	{
		return maps[idx];
	}

	/**
	 * Returns a list of names
	 * @return String[] with the specific names of a ColorMap
	 */
	public static String[] getNames()
	{
		String[] names = new String[maps.length];
		for (int i = 0; i < maps.length; i++)
			names[i] = maps[i].getName();
		return names;
	}


	private static ColorMapDefinition defineMap(String name, int[][] colorValues)
	{
		return new ColorMapDefinition(name, colorValues);
	}

	private static class ColorMapDefinition
	{
		String name;
		int[][] colorValues;

		public ColorMapDefinition(String name, int[][] colorValues)
		{
			this.name = name;
			this.colorValues = colorValues;
		}
	}
}
