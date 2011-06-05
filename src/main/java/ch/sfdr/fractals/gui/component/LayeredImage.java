package ch.sfdr.fractals.gui.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


/**
 * A layered image as a non-visual component.
 * @author D.Ritz
 */
public class LayeredImage
	implements ImageDisplay
{
	private static final Color TRANSPARENT_BACK = new Color(255, 255, 255, 0);

	private int width;
	private int height;

	private BufferedImage bufferImage;
	private Graphics2D bufferGraphics;

	private List<Layer> layers = new ArrayList<Layer>();

	/**
	 * Creates a layered image with the specified number of layers
	 * @param numLayers number of layers
	 */
	public LayeredImage(int numLayers)
	{
		for (int i = 0; i < numLayers; i++)
			layers.add(new Layer());
	}

	/**
	 * Returns the backing BufferedImage. Use this to paint on a visible component.
	 * @return BufferedImage
	 */
	public BufferedImage getBackingImage()
	{
		return bufferImage;
	}

	@Override
	public int getImageWidth()
	{
		return width;
	}

	@Override
	public int getImageHeight()
	{
		return height;
	}

	/**
	 * creates the buffered images backing this component
	 */
	public synchronized void createImages(int width, int height)
	{
		this.width = width;
		this.height = height;

		if (bufferGraphics != null)
			bufferGraphics.dispose();
		if (bufferImage != null)
			bufferImage.flush();

		bufferImage = createImage();
		bufferGraphics = bufferImage.createGraphics();

		for (Layer l : layers)
			l.createImage();

		repaintAllLayers();
	}

	@Override
	public BufferedImage createImage()
	{
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	public int getLayers()
	{
		return layers.size();
	}

	@Override
	public int addLayer()
	{
		Layer l = new Layer();
		if (bufferImage != null)
			l.createImage();
		layers.add(l);
		return layers.size() - 1;
	}

	@Override
	public synchronized void removeLayer(int layer)
	{
		layers.remove(layer);
		repaintAllLayers();
	}

	@Override
	public synchronized void clearLayer(int layer)
	{
		layers.get(layer).clear();
		repaintAllLayers();
	}

	@Override
	public synchronized BufferedImage getLayerImage(int layer)
	{
		return layers.get(layer).image;
	}

	@Override
	public Graphics2D getLayerGraphics(int layer)
	{
		return layers.get(layer).graphics;
	}

	@Override
	public synchronized void updateImage(BufferedImage img, int layer)
	{
		if (img == null)
			return;
		layers.get(layer).updateImage(img);
		repaintAllLayers();
	}

	private void repaintAllLayers()
	{
		for (Layer l : layers)
			bufferGraphics.drawImage(l.image, 0, 0, null);
		newImageAvailable();
	}

	/**
	 * Called whenever a new image is available. Can be used to repaint() a
	 * visual component.
	 */
	protected void newImageAvailable()
	{

	}

	@Override
	public synchronized void updateLayer(int layer)
	{
		repaintAllLayers();
	}

	@Override
	public synchronized void saveImage(File file, String format)
		throws IOException
	{
		/*
		 * bufferImage is of type RGBA (with alpha), but JPEG does not support
		 * it. So it needs to be translated to RGB (without alpha) first.
		 */
		BufferedImage saveImg = new BufferedImage(bufferImage.getWidth(),
			bufferImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		saveImg.createGraphics().drawImage(bufferImage, 0, 0, null);

		ImageIO.write(saveImg, format, file);
	}

	/**
	 * a single layer
	 */
	private class Layer
	{
		private BufferedImage image;
		private Graphics2D graphics;

		public void updateImage(BufferedImage img)
		{
			graphics.drawImage(img, 0, 0, null);
		}

		public void createImage()
		{
			if (graphics != null)
				graphics.dispose();
			if (image != null)
				image.flush();

			image = new BufferedImage(getImageWidth(), getImageHeight(),
				BufferedImage.TYPE_INT_ARGB);
			graphics = image.createGraphics();
		}

		public void clear()
		{
			graphics.setBackground(TRANSPARENT_BACK);
			graphics.clearRect(0, 0, image.getWidth(), image.getHeight());
		}
	}
}
