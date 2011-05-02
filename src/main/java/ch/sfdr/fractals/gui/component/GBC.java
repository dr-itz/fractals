package ch.sfdr.fractals.gui.component;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * GBC, a small helper class to create GridBagConstraints in a more readable way
 * with less typing: just use the static methods when adding a component, eg.
 * <code>
 * 		container.add(someComponent, GBC.get(0, 0, 1, 1, 'b'));
 * </code>
 * Imported from another (old) project, adopted a bit
 */
public final class GBC
{
	private static char DEFAULT_FILL = 'n';

	private static String DEFAULT_ANCHOR = "W";

	private static String[] ANCHOR_STRINGS = {
		"n", "ne", "e", "se", "s", "sw", "w", "nw", "c"
	};

	private static int[] ANCHOR_VALUES = {
		GridBagConstraints.NORTH, GridBagConstraints.NORTHEAST,
		GridBagConstraints.EAST, GridBagConstraints.SOUTHEAST,
		GridBagConstraints.SOUTH, GridBagConstraints.SOUTHWEST,
		GridBagConstraints.WEST, GridBagConstraints.NORTHWEST,
		GridBagConstraints.CENTER
	};

	private static int getAnchor(String str)
	{
		str = str.toLowerCase();
		for (int i = 0; i < ANCHOR_STRINGS.length; i++) {
			if (str.equals(ANCHOR_STRINGS[i]))
				return ANCHOR_VALUES[i];
		}
		return -1;
	}

	private static int getFill(char c)
	{
		switch (c) {
		case 'n':
		case 'N':
			return GridBagConstraints.NONE;
		case 'v':
		case 'V':
			return GridBagConstraints.VERTICAL;
		case 'h':
		case 'H':
			return GridBagConstraints.HORIZONTAL;
		case 'b':
		case 'B':
			return GridBagConstraints.BOTH;
		}
		return -1;
	}

	/**
	 * Returns a GridBagConstraint, setting all values directly
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param wx
	 * @param wy
	 * @param insetTop
	 * @param insetLeft
	 * @param insetBottom
	 * @param insetRight
	 * @param fill
	 * @param anchor
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			double wx, double wy, int insetTop, int insetLeft, int insetBottom,
			int insetRight, char fill, String anchor)
	{
		return new GridBagConstraints(x, y, width, height,
			wx, wy, getAnchor(anchor), getFill(fill),
			new Insets(insetTop, insetLeft, insetBottom, insetRight),
			0, 0);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param wx
	 * @param wy
	 * @param fill
	 * @param anchor
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			double wx, double wy, char fill, String anchor)
	{
		return get(x, y, width, height, wx, wy, 5, 5, 5, 5, fill, anchor);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param wx
	 * @param wy
	 * @param fill
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			double wx, double wy, char fill)
	{
		return get(x, y, width, height, wx, wy, fill, DEFAULT_ANCHOR);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param wx
	 * @param wy
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			double wx, double wy)
	{
		return get(x, y, width, height, wx, wy, DEFAULT_FILL, DEFAULT_ANCHOR);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param wx
	 * @param wy
	 * @param anchor
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			double wx, double wy, String anchor)
	{
		return get(x, y, width, height, wx, wy, DEFAULT_FILL, anchor);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fill
	 * @param anchor
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			char fill, String anchor)
	{
		return get(x, y, width, height, 0.0, 0.0, fill, anchor);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param fill
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			char fill)
	{
		return get(x, y, width, height, 0.0, 0.0, fill, DEFAULT_ANCHOR);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height)
	{
		return get(x, y, width, height, 0.0, 0.0, DEFAULT_FILL, DEFAULT_ANCHOR);
	}

	/**
	 * Returns a GridBagConstraint
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param anchor
	 * @return GridBagConstraints
	 */
	public static GridBagConstraints get(int x, int y, int width, int height,
			String anchor)
	{
		return get(x, y, width, height, 0.0, 0.0, DEFAULT_FILL, anchor);
	}
}
