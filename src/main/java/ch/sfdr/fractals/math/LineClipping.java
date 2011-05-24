package ch.sfdr.fractals.math;

/**
 * 2D Line clipping based on the well-known Cohen–Sutherland line clipping
 * algorithm. This is not the most efficient algorithm, but performs well
 * enough. For a C++ version, see:
 *   http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland
 * This is a Java version of it.
 * @author D.Ritz
 */
public class LineClipping
{
	// region bit masks
	private static final byte INSIDE	= 0x00;
	private static final byte BOTTOM	= 0x01;
	private static final byte LEFT		= 0x02;
	private static final byte RIGHT		= 0x04;
	private static final byte TOP		= 0x08;

	private double clipX1, clipX2, clipY1, clipY2;

	private static byte calculateCode(double x, double y, long xmin, long ymin,
			long xmax, long ymax)
	{
		byte code = INSIDE;
		if (x < xmin)
			code |= LEFT;
		else if (x > xmax)
			code |= RIGHT;
		if (y < ymin)
			code |= BOTTOM;
		else if (y > ymax)
			code |= TOP;
		return code;
	}

	/**
	 * simple check if a single point is within the clipping rect
	 * @param x
	 * @param y
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @return
	 */
	public static boolean isPointInside(double x, double y, long xmin, long ymin,
			long xmax, long ymax)
	{
		return calculateCode(x, y, xmin, ymin, xmax, ymax) == INSIDE;
	}

	/**
	 * Clips a line to a rectangle. Returns true if the line is visible at all,
	 * false otherwise. The clipped coordinates are accessible via the getters.
	 * @param x1 point1 x
	 * @param y1 point1 y
	 * @param x2 point2 x
	 * @param y2 point2 y
	 * @param xmin clipping rect left
	 * @param ymin clipping rect left
	 * @param xmax clipping rect left
	 * @param ymax clipping rect left
	 * @return
	 */
	public boolean clipLineToRectangle(double x1, double y1, double x2,
			double y2, long xmin, long ymin, long xmax, long ymax)
	{
		clipX1 = x1;
		clipY1 = y1;
		clipX2 = x2;
		clipY2 = y2;

		// region codes...
		byte c1 = calculateCode(x1, y1, xmin, ymin, xmax, ymax);
		byte c2 = calculateCode(x2, y2, xmin, ymin, xmax, ymax);

		while (true) {
			if ((c1 | c2) == 0) {
				return true;
			} else if ((c1 & c2) > 0) {
				// both points are outside the rectangle in the same region
				return false;
			}
			double x, y;
			// At least one point is outside the clip rectangle; pick it.
			byte c = c1 != 0 ? c1 : c2;
			// first point is outside the rectangle
			if ((c & TOP) > 0) {
				x = clipX1 + (clipX2 - clipX1) * (ymax - clipY1) /
					(clipY2 - clipY1);
				y = ymax;
			} else if ((c & BOTTOM) > 0) {
				x = clipX1 + (clipX2 - clipX1) * (ymin - clipY1) /
					(clipY2 - clipY1);
				y = ymin;
			} else if ((c & RIGHT) > 0) {
				y = clipY1 + (clipY2 - clipY1) * (xmax - clipX1) /
					(clipX2 - clipX1);
				x = xmax;
			} else {
				y = clipY1 + (clipY2 - clipY1) * (xmin - clipX1) /
					(clipX2 - clipX1);
				x = xmin;
			}
			if (c == c1) {
				clipX1 = x;
				clipY1 = y;
				c1 = calculateCode(x, y, xmin, ymin, xmax, ymax);
			} else {
				clipX2 = x;
				clipY2 = y;
				c2 = calculateCode(x, y, xmin, ymin, xmax, ymax);
			}
		}
	}

	/**
	 * @return the clipX1
	 */
	public int getClipX1()
	{
		return (int) Math.round(clipX1);
	}

	/**
	 * @return the clipX2
	 */
	public int getClipX2()
	{
		return (int) Math.round(clipX2);
	}

	/**
	 * @return the clipY1
	 */
	public int getClipY1()
	{
		return (int) Math.round(clipY1);
	}

	/**
	 * @return the clipY2
	 */
	public int getClipY2()
	{
		return (int) Math.round(clipY2);
	}
}
