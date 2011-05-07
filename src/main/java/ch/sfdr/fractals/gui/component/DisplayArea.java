package ch.sfdr.fractals.gui.component;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * The fractal display area, thread safe
 */
public class DisplayArea
	extends JPanel
	implements ImageDisplay
{
	private static final long serialVersionUID = 1L;

	private Image bufferImage;
	private Graphics bufferGraphics;

	private List<Layer> layers = new ArrayList<Layer>();

	/**
	 * creates the DisplayArea
	 */
	public DisplayArea(int numLayers)
	{
		super();
		for (int i = 0; i < numLayers; i++)
			layers.add(new Layer());
	}

	/**
	 * creates the buffered images backing this component
	 */
	public synchronized void createImages()
	{
		if (bufferGraphics != null)
			bufferGraphics.dispose();
		if (bufferImage != null)
			bufferImage.flush();

		bufferImage = createImage(getImageWidth(), getImageHeight());
		bufferGraphics = bufferImage.getGraphics();

		for (Layer l : layers)
			l.createImage();
	}

	/*
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g)
	{
		if (bufferImage == null) {
			super.paint(g);
			return;
		}

		g.drawImage(bufferImage, 0, 0, null);
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#createImage()
	 */
	@Override
	public synchronized BufferedImage createImage()
	{
		return new BufferedImage(getImageWidth(), getImageHeight(),
			BufferedImage.TYPE_INT_ARGB);
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#getImageHeight()
	 */
	@Override
	public int getImageHeight()
	{
		return getSize().height;
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#getImageWidth()
	 */
	@Override
	public int getImageWidth()
	{
		return getSize().width;
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#updateImage(java.awt.Image)
	 */
	@Override
	public synchronized void updateImage(Image img, int layer)
	{
		layers.get(layer).updateImage(img);

		for (Layer l : layers)
			bufferGraphics.drawImage(l.getImage(), 0, 0, null);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				repaint();
			}
		});
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#getLayers()
	 */
	@Override
	public int getLayers()
	{
		return layers.size();
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#addLayer()
	 */
	@Override
	public int addLayer()
	{
		Layer l = new Layer();
		l.createImage();
		layers.add(l);
		return layers.size() - 1;
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#removeLayer(int)
	 */
	@Override
	public void removeLayer(int layer)
	{
		layers.remove(layer);
	}

	/*
	 * @see ch.sfdr.fractals.gui.component.ImageDisplay#clearLayer(int)
	 */
	@Override
	public void clearLayer(int layer)
	{
		layers.get(layer).createImage();
	}

	/**
	 * a single layer
	 */
	private class Layer
	{
		private BufferedImage image;
		private Graphics2D graphics;

		public void updateImage(Image img)
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

		public BufferedImage getImage()
		{
			return image;
		}
	}
}
