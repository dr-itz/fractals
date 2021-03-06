package ch.sfdr.fractals.fractals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import ch.sfdr.fractals.gui.component.ColorMap;
import ch.sfdr.fractals.gui.component.ImageDisplay;
import ch.sfdr.fractals.math.ComplexNumber;
import ch.sfdr.fractals.math.LineClipping;
import ch.sfdr.fractals.math.Scaler;

/**
 * A fractal based on escape time algorithm
 * @author S.Freihofer
 * @author D.Ritz
 */
public class ComplexEscapeFractal
{
	private ImageDisplay display;
	private Scaler scaler;
	private StepFractalFunction function;
	private ColorMap colorMap;
	private Color setColor = Color.BLACK;

	private Thread thread;

	private double boundarySqr;

	private StatisticsObserver statObserver;
	private Object statLock = new Object();
	private long stepCount;
	private long drawTime;
	private long pixelsCalculated;
	private long pixelsToCalculate;
	private int lastPercentage;

	private ArrayList<Orbit> orbitList = new ArrayList<Orbit>();
	private Thread orbitThread;
	private boolean hasLiveOrbit = false;
	private ComplexNumber liveOrbitStart = new ComplexNumber(0, 0);

	/**
	 * Creates the ComplexEscapeFractal
	 * @param display the display area to draw on
	 * @param scaler the scaler to convert between screen and fractal coordinates
	 * @param function the function to use
	 */
	public ComplexEscapeFractal(ImageDisplay display, Scaler scaler,
			StepFractalFunction function, ColorMap colorMap)
	{
		this.display = display;
		int layers = display.getLayers();
		for (int i = 0; i < 3 - layers; i++)
			display.addLayer();
		this.scaler = scaler;
		this.colorMap = colorMap;
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
	 * Returns the currently used fractal function
	 * @return the function
	 */
	public StepFractalFunction getFunction()
	{
		return function;
	}

	/**
	 * Sets the specific ColorMap
	 * @param colorMap the colorMap to set
	 */
	public void setColorMap(ColorMap colorMap)
	{
		this.colorMap = colorMap;
	}

	/**
	 * Sets the set color
	 * @param setColor the setColor to set
	 */
	public void setSetColor(Color setColor)
	{
		this.setColor = setColor;
	}

	/**
	 * Sets the StatisticsObserver
	 * @param statObserver the statObserver to set
	 */
	public void setStatObserver(StatisticsObserver statObserver)
	{
		this.statObserver = statObserver;
	}

	/**
	 * Gets the StatisticsObserver
	 * @return the statObserver
	 */
	public StatisticsObserver getStatObserver()
	{
		return statObserver;
	}

	/**
	 * Draws the fractal with a max number of iterations per point
	 * @param maxIterations the max number of iterations
	 * @throws InterruptedException
	 */
	public synchronized void drawFractal(final int maxIterations)
	{
		// if there's a previous thread: interrupt and wait for it
		if (thread != null) {
			thread.interrupt();
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
		}

		// create and start the new thread
		thread = new Thread() {
			@Override
			public void run()
			{
				long start = System.currentTimeMillis();

				doDrawFractal(maxIterations);

				drawTime = System.currentTimeMillis() - start;

				if (statObserver != null)
					statObserver.statisticsDataAvailable(ComplexEscapeFractal.this);
			}
		};
		thread.start();
	}

	/**
	 * waits for the fractal drawing to complete
	 */
	public synchronized void waitForFractalCompleted()
	{
		if (thread != null) {
			try {
				thread.join();
			} catch (InterruptedException e) {
			}
			thread = null;
		}
	}



	/**
	 * Draws the orbit for the given coordinates and step delay
	 * @param start the start coordinate as complex number
	 * @param maxIterations the max number of iterations
	 * @param color the color used to draw the orbit
	 * @param stepDelay the delay in milliseconds
	 */
	public void drawOrbit(final ComplexNumber start, final int maxIterations,
			final Color color, final long stepDelay)
	{
		if (hasLiveOrbit) {
			display.clearLayer(2);
			hasLiveOrbit = false;
		}

		waitForOrbitCompleted(true);

		orbitList.add(new Orbit(start, color, maxIterations));

		orbitThread = new Thread() {
			@Override
			public void run()
			{
				doDrawOrbit(1, start, maxIterations, color, stepDelay, true);
			}
		};
		orbitThread.start();
	}

	/**
	 * waits for the fractal drawing to complete.
	 * @param interrupt if true, thread is told to complete drawing w/o delays
	 */
	public synchronized void waitForOrbitCompleted(boolean interrupt)
	{
		if (orbitThread != null) {
			if (interrupt)
				orbitThread.interrupt();
			try {
				orbitThread.join();
			} catch (InterruptedException e) {
			}
			orbitThread = null;
		}
	}

	/**
	 * Draws a live orbit, e.g. during mouse drag
	 * @param x start point x coordinate
	 * @param y start point y coordinate
	 * @param maxIterations maximum number of iterations
	 */
	public void drawLiveOrbit(int x, int y, int maxIterations)
	{
		liveOrbitStart.set(scaler.scaleX(x), scaler.scaleY(y));
		hasLiveOrbit = true;

		display.clearLayer(2);
		doDrawOrbit(2, liveOrbitStart, maxIterations, Color.WHITE, 0, true);
	}

	private void doDrawFractal(final int maxIterations)
	{
		stepCount = 0;
		pixelsCalculated = 0;
		lastPercentage = 0;

		final int width = display.getImageWidth();
		final int height = display.getImageHeight();
		pixelsToCalculate = width * height;

		final BufferedImage imgFine = display.createImage();
		final Graphics2D gFine = imgFine.createGraphics();

		// start the last (fine) drawing step first
		Thread fineThread = new Thread("Fractal fine drawing") {
			@Override
			public void run()
			{
				drawFractalStep(gFine, maxIterations, width, height, 1, 2);
			}
		};
		fineThread.start();

		// draw all but the last step: 16, 8, 4, 2 squares
		BufferedImage img = display.createImage();
		Graphics2D g = img.createGraphics();

		boolean cont = true;
		for (int step = 16, oldStep = 32; step > 1 && cont; oldStep = step, step /= 2) {
			cont = drawFractalStep(g, maxIterations, width, height, step, oldStep);
			if (cont)
				display.updateImage(img, 0);
		}

		if (!cont)
			fineThread.interrupt();

		// wait for the fine thread, draw it's image
		try {
			fineThread.join();
		} catch (InterruptedException e) {
		}
		if (cont)
			display.updateImage(imgFine, 0);
	}

	private boolean drawFractalStep(Graphics2D g, int maxIterations,
			int width, int height, int step, int oldStep)
	{
		// the only two, reusable complex numbers
		ComplexNumber z0 = new ComplexNumber(0, 0);
		ComplexNumber z = new ComplexNumber(0, 0);

		long threadStepCount = 0;

		// outer loop: rows
		for (int y = 0; y < height; y += step) {
			int start = 0;
			int inc = step;
			// skip pixel already drawn in last iteration
			if (step < 16 && y % oldStep == 0) {
				start = step;
				inc = oldStep;
			}

			if (Thread.interrupted())
				return false;

			int pixels = 0;
			double fractalY = scaler.scaleY(y);

			// inner loop: pixels
			for (int x = start; x < width; x += inc) {
				double fractalX = scaler.scaleX(x);

				// set the z0 and the variable z to the current values
				z0.set(fractalX, fractalY);
				z.set(fractalX, fractalY);

				// get iteration count using the step() function
				int count = 0;
				while (z.absSqr() < boundarySqr && count++ < maxIterations)
					function.step(z0, z);

				threadStepCount += count;
				pixels++;

				// map to color and draw pixel
				Color color = getColor(count, maxIterations);
				g.setColor(color);
		        g.fillRect(x, y, step, step);
			}

			synchronized (statLock) {
				pixelsCalculated += pixels;
				int p = (int) (100 * pixelsCalculated / pixelsToCalculate);
				if (p != lastPercentage) {
					lastPercentage = p;
					if (statObserver != null)
						statObserver.updateProgess(ComplexEscapeFractal.this, p);
				}
			}
		}

		synchronized (statLock) {
			stepCount += threadStepCount;
		}
		return true;
	}

	private void doDrawOrbit(int layer, ComplexNumber orbitStart,
			int maxIterations, Color orbitColor, long orbitDelay,
			boolean updateImage)
	{
		ComplexNumber z0 = orbitStart;
		ComplexNumber z = z0.clone();

		LineClipping lc = new LineClipping();

		Graphics2D g = display.getLayerGraphics(layer);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(orbitColor);

		int width = display.getImageWidth();
		int height = display.getImageHeight();

		double lastX = scaler.unscaleX(z0.getReal());
		double lastY = scaler.unscaleY(z0.getImaginary());

		// draw a little circle indicating the starting point
		if (LineClipping.isPointInside(lastX, lastY, 0, 0, width, height)) {
			g.setStroke(new BasicStroke(1.5f));
			g.drawOval((int) Math.round(lastX) - 5, (int) Math.round(lastY) - 5,
				10, 10);
		}

		// a thinner stroke for the lines
		g.setStroke(new BasicStroke(0.7f));

		int count = 0;
		while (z.absSqr() < boundarySqr && count++ < maxIterations) {
			function.step(z0, z);

			double x = scaler.unscaleX(z.getReal());
			double y = scaler.unscaleY(z.getImaginary());

			if (lc.clipLineToRectangle(lastX, lastY, x, y, 0, 0, width, height)) {
				g.drawLine(lc.getClipX1(), lc.getClipY1(),
					lc.getClipX2(), lc.getClipY2());

				// only show the first 30 steps "animated", the rest in one go
				if (Thread.interrupted())
					orbitDelay = 0;

				if (count < 30 && orbitDelay > 0) {
					display.updateLayer(layer);

					try {
						Thread.sleep(orbitDelay);
					} catch (InterruptedException e) {
						orbitDelay = 0;
					}
				}
			}

			lastX = x;
			lastY = y;
		}
		if (updateImage)
			display.updateLayer(layer);
	}

	private Color getColor(int count, int maxIterations)
	{
		if (count >= maxIterations)
			return setColor;
		return colorMap.getColor(count);
	}

	/**
	 * Gets the count of all steps
	 * @return the stepCount
	 */
	public long getStepCount()
	{
		return stepCount;
	}

	/**
	 * Gets the time to draw in milliseconds
	 * @return the drawTime
	 */
	public long getDrawTime()
	{
		return drawTime;
	}

	private static class Orbit
	{
		private ComplexNumber start;
		private Color color;
		private int itarations;

		public Orbit(ComplexNumber start, Color color, int iterations)
		{
			this.start = start;
			this.color = color;
			this.itarations = iterations;
		}
	}

	/**
	 * Used to draw the saved orbits again, after a zoom
	 */
	public void redrawAllOrbits()
	{
		if (orbitList.isEmpty())
			return;

		for (Orbit o : orbitList) {
			doDrawOrbit(1, o.start, o.itarations, o.color, 0, false);
		}
		display.updateLayer(1);
	}

	/**
	 * Removes all saved orbits from the list
	 */
	public void clearOrbits()
	{
		orbitList.clear();
		display.clearLayer(1);
		display.clearLayer(2);
	}
}
