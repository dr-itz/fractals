package ch.sfdr.fractals.math;

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
		return yrange * (y * r + viewY) + ymin + offsetY;
	}
}
