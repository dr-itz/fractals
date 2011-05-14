package ch.sfdr.fractals.math;

import java.awt.Rectangle;

/**
 * Scaler - Conversion between screen coordinates and scaled numbers with
 * zooming.
 */
public class Scaler
{
	// the displayed range
	private double xmin;
	private double ymin;

	private double xrange;
	private double yrange;

	// the real pictures dimensions
	private int width;
	private int height;

	// the offsets in the scaled view
	private double viewX;
	private double viewY;

	// the offsets used to center the view
	private double offsetX;
	private double offsetY;

	// zoom
	private double zoom;

	// internal scaling factor
	private double r;

	/**
	 * initializes for the specified range
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 */
	public void init(double xmin, double ymin, double xmax, double ymax)
	{
		this.xmin = xmin;
		this.ymin = ymin;

		zoom = 1.0D;
		viewX = 0.0D;
		viewY = 0.0D;

		xrange = Math.abs(xmax - xmin);
		yrange = Math.abs(ymax - ymin);
	}

	/**
	 * sets the dimensions of the real images
	 * @param width the image width in pixels
	 * @param height the image height in pixels
	 */
	public void setDimension(int width, int height)
	{
		this.width = width;
		this.height = height;


		preCalculate();
	}

	private void preCalculate()
	{
		// the scaling factor
		r = 1.0D / (double) Math.min(width, height) / zoom;

		// offset used to center
		offsetX = 0.0D;
		offsetY = 0.0D;
		if (width > height) {
			offsetX = 2.0D * r * (width - height);
		} else if (height > width) {
			offsetY = 2.0D * r * (height - width);
		}
	}

	/**
	 * zooms into the selected rectangle
	 * @param rect
	 */
	public void zoomIn(Rectangle rect)
	{
		// offset in x/y direction
		double sx = width > height ? width - height : 0.0;
		double sy = height > width ? height - width : 0.0;
		viewX += r * (rect.x - sx / 2.0D);
		viewY += r * (rect.y - sy / 2.0D);

		// new zoom
		zoom *= Math.max((double) width / rect.width,
			(double) height / rect.height);

		// calculate new r and offsets
		preCalculate();

		// correct the offsets for new zoom
		viewX += r * sx / 2.0D;
		viewY += r * sy / 2.0D;
	}

	/**
	 * resets the zoom to 1.0
	 */
	public void resetZoom()
	{
		zoom = 1.0D;
		viewX = 0.0D;
		viewY = 0.0D;
		preCalculate();
	}

	/**
	 * returns the current level of zoom
	 * @return current zoom
	 */
	public double getZoom()
	{
		return zoom;
	}

	/**
	 * scales an x value to the scaled images double
	 * @param x image x coordinate
	 * @return scaled x coordinate
	 */
	public double scaleX(int x)
	{
		return xrange * (x * r + viewX) + xmin - offsetX;
	}

	/**
	 * scales an y value to the scaled images double
	 * @param y image y coordinate
	 * @return scaled y coordinate
	 */
	public double scaleY(int y)
	{
		return yrange * (y * r + viewY) + ymin - offsetY;
	}

	/**
	 * calculates the screen coordinate for a scaled number
	 * @param scaledX the scaled number
	 * @return the screen coordinate
	 */
	public int unscaleX(double scaledX)
	{
		double t = ((scaledX + offsetX - xmin) / xrange - viewX) / r;
		return (int) Math.round(t);
	}

	/**
	 * calculates the screen coordinate for a scaled number
	 * @param scaledX the scaled number
	 * @return the screen coordinate
	 */
	public int unscaleY(double scaledY)
	{
		double t = ((scaledY + offsetY - ymin) / yrange - viewY) / r;
		return (int) Math.round(t);
	}
}
