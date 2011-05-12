package ch.sfdr.fractals.fractals;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import ch.sfdr.fractals.gui.component.ImageDisplay;
import ch.sfdr.fractals.math.ComplexNumber;
import ch.sfdr.fractals.math.Scaler;

/**
 * A fractal based on escape time algorithm
 * @author S.Freihofer
 */
public class ComplexEscapeFractal
{
	private ImageDisplay display;
	private Scaler scaler;
	private StepFractalFunction function;

	private double boundarySqr;
	private int maxIterations;

	/**
	 * Creates the ComplexEscapeFractal
	 * @param display the display area to draw on
	 * @param scaler the scaler to convert between screen and fractal coordinates
	 * @param function the function to use
	 */
	public ComplexEscapeFractal(ImageDisplay display, Scaler scaler,
			StepFractalFunction function)
	{
		this.display = display;
		this.scaler = scaler;
		setFractalFunction(function);
	}

	/**
	 * Sets the fractal function and initializes the scaler
	 * @param function the function to use
	 */
	public void setFractalFunction(StepFractalFunction function)
	{
		this.function = function;
		boundarySqr = function.getBoundarySqr();
		scaler.init(
			function.getLowerBounds().getReal(),
			function.getLowerBounds().getImaginary(),
			function.getUpperBounds().getReal(),
			function.getUpperBounds().getImaginary());
	}

	/**
	 * Draws the fractal with a max number of iterations per point
	 * @param maxIterations the max number of iterations
	 */
	public void drawFractal(int maxIterations)
	{
		this.maxIterations = maxIterations;
		Thread thread = new Thread() {
			@Override
			public void run()
			{
				doDrawFractal();
			}
		};
		thread.start();
	}

	private void doDrawFractal()
	{
		int width = display.getImageWidth();
		int height = display.getImageHeight();

		scaler.setDimension(width, height);

		BufferedImage img = display.createImage();
		Graphics2D g = img.createGraphics();

		// the only two, reusable complex numbers
		ComplexNumber z0 = new ComplexNumber(0, 0);
		ComplexNumber z = new ComplexNumber(0, 0);

		/*
		 * main loop for all pixels
		 */
		// outer loop: rows
		for (int y = 0; y < height; y++) {

			double fractalY = scaler.scaleY(y);

			// inner loop: pixels
			for (int x = 0; x < width; x++) {
				double fractalX = scaler.scaleX(x);

				// set the z0 and the variable z to the current values
				z0.set(fractalX, fractalY);
				z.set(fractalX, fractalY);

				// get iteration count using the step() function
				int count = 0;
				while (z.absSqr() < boundarySqr && count++ < maxIterations)
					function.step(z0, z);

				// map to color and draw pixel
				Color color = getColor(count);
				g.setColor(color);
		        g.fillRect(x, y, 1, 1);
			}
		}
		display.updateImage(img, 0);
	}

	private Color getColor(int count)
	{
		if (count >= maxIterations)
			return Color.BLACK;
		return Color.WHITE;
	}
}
