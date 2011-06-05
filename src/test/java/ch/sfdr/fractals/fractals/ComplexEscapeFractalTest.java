package ch.sfdr.fractals.fractals;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import ch.sfdr.fractals.gui.component.ColorMap;
import ch.sfdr.fractals.gui.component.ColorMapFactory;
import ch.sfdr.fractals.gui.component.LayeredImage;
import ch.sfdr.fractals.math.ComplexNumber;
import ch.sfdr.fractals.math.Scaler;

/**
 * Tests ComplexEscapeFractal using Mandelbrot
 * @author D.Ritz
 */
public class ComplexEscapeFractalTest
	implements StatisticsObserver
{
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	private static final File failName = new File("failed-test.png");

	private static final File refDir = new File("src/test/resource");

	private static final File refFractalName = new File(refDir, "fractal.png");
	private static final File refFractal2Name = new File(refDir, "fractal2.png");
	private static final File refOrbitName = new File(refDir, "orbit.png");
	private static final File refLiveOrbitName = new File(refDir, "liveOrbit.png");

	private ComplexEscapeFractal me;
	private Scaler scaler;
	private LayeredImage image;

	private long stepCount = 0;
	private long drawTime = 0;

	@Before
	public void setUp()
		throws Exception
	{
		if (failName.exists() && !failName.delete())
			fail("failed to delete: " + failName);

		scaler = new Scaler();
		scaler.setDimension(WIDTH, HEIGHT);
		ColorMap colorMap = ColorMapFactory.getMap(0); // the grayscale map
		Mandelbrot mandelbrot = new Mandelbrot();
		image = new LayeredImage(1);
		image.createImages(WIDTH, HEIGHT);

		me = new ComplexEscapeFractal(image, scaler, mandelbrot, colorMap);

		assertEquals(mandelbrot, me.getFunction());
		assertNull(me.getStatObserver());
	}

	@Override
	public void statisticsDataAvailable(Object source)
	{
		drawTime = me.getDrawTime();
		stepCount = me.getStepCount();
	}

	@Override
	public void updateProgess(Object source, int percent)
	{
		assertEquals(me, source);
	}

	private void drawFractal()
	{
		me.drawFractal(200);
		me.waitForFractalCompleted();
	}

	@Test
	public void testDrawFractal()
	{
		BufferedImage ref = loadRefImage(refFractalName);

		me.setStatObserver(this);
		assertEquals(this, me.getStatObserver());

		drawFractal();
		me.setStatObserver(null);

		compareImages(ref, image.getBackingImage());

		assertTrue(drawTime > 0);
		assertEquals(3412073, stepCount);
	}


	private void drawOrbit()
	{
		me.drawOrbit(
			new ComplexNumber(-0.009523809523809379D, -0.647619047619048D),
			200, Color.RED, 0);
		me.waitForOrbitCompleted(true);
	}


	@Test
	public void testDrawOrbit()
	{
		BufferedImage ref = loadRefImage(refOrbitName);

		drawOrbit();
		compareImages(ref, image.getBackingImage());

		reset();

		me.redrawAllOrbits();
		me.waitForOrbitCompleted(true);
		compareImages(ref, image.getBackingImage());
	}

	@Test
	public void testClearOrbits()
	{
		BufferedImage ref = loadRefImage(refFractalName);

		drawFractal();
		drawOrbit();
		me.clearOrbits();

		compareImages(ref, image.getBackingImage());
	}

	private void drawFractalDifferentColors()
	{
		me.setColorMap(ColorMapFactory.getMap(1));
		me.setSetColor(Color.RED);
		drawFractal();
	}

	@Test
	public void testDifferentColors()
	{
		BufferedImage ref = loadRefImage(refFractal2Name);
		drawFractalDifferentColors();
		compareImages(ref, image.getBackingImage());
	}


	private void drawLiveOrbit()
	{
		me.drawLiveOrbit(200, 266, 200);
		me.waitForOrbitCompleted(true);
	}

	@Test
	public void testDrawLiveOrbit()
	{
		BufferedImage ref = loadRefImage(refLiveOrbitName);
		drawLiveOrbit();
		compareImages(ref, image.getBackingImage());
	}

	//--------------------------------------------------------------------------

	private BufferedImage convertRGB(BufferedImage in)
	{
		BufferedImage out = new BufferedImage(in.getWidth(),
			in.getHeight(), BufferedImage.TYPE_INT_RGB);
		out.createGraphics().drawImage(in, 0, 0, null);
		return out;
	}

	private void compareImages(BufferedImage expected, BufferedImage actual)
	{
		try {
			ImageIO.write(actual, "png", failName);
		} catch (IOException e) {
			fail("failed to save actual image: " + e);
		}

		expected = convertRGB(expected);
		actual = convertRGB(actual);

		assertEquals(expected.getWidth(), actual.getWidth());
		assertEquals(expected.getHeight(), actual.getHeight());

		DataBuffer db1 = expected.getData().getDataBuffer();
		DataBuffer db2 = actual.getData().getDataBuffer();
		assertEquals(db1.getSize(), db2.getSize());
		for (int i = 0; i < db1.getSize(); i++)
			assertEquals(db1.getElem(i), db2.getElem(i));

		failName.delete();
	}

	private BufferedImage loadRefImage(File name)
	{
		try {
			return ImageIO.read(name);
		} catch (IOException e) {
			fail(e.toString());
		}
		return null; // never reached
	}


	//--------------------------------------------------------------------------

	private void reset()
	{
		image.createImages(WIDTH, HEIGHT);
	}

	private void saveRefFractal()
		throws IOException
	{
		drawFractal();
		image.saveImage(refFractalName, "png");
	}

	private void saveRefOrbits()
		throws IOException
	{
		drawOrbit();
		image.saveImage(refOrbitName, "png");
	}

	private void saveRefLiveOrbits()
		throws IOException
	{
		drawLiveOrbit();
		image.saveImage(refLiveOrbitName, "png");
	}

	private void saveRefFractalDifferentColors()
		throws IOException
	{
		drawFractalDifferentColors();
		image.saveImage(refFractal2Name, "png");
	}


	/**
	 * main method used to save reference images
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args)
		throws Exception
	{
		refDir.mkdirs();

		ComplexEscapeFractalTest test = new ComplexEscapeFractalTest();
		test.setUp();
		test.saveRefFractal();
		test.reset();
		test.saveRefOrbits();
		test.reset();
		test.saveRefFractalDifferentColors();
		test.reset();
		test.saveRefLiveOrbits();
	}
}
