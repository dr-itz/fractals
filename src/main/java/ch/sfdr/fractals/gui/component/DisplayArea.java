package ch.sfdr.fractals.gui.component;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * The fractal display area, thread safe
 */
public class DisplayArea
	extends JPanel
	implements ImageDisplay
{
	private Image bufferImage;
	private Graphics bufferGraphics;

	private static final long serialVersionUID = 1L;

	/**
	 * creates the DisplayArea
	 */
	public DisplayArea()
	{
		super();
	}

	/**
	 * creates the buffered images backing this component
	 */
	public synchronized void createImages()
	{
		bufferImage = createImage(getImageWidth(), getImageHeight());
		bufferGraphics = bufferImage.getGraphics();
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
	public synchronized void updateImage(Image img)
	{
		bufferGraphics.drawImage(img, 0, 0, null);
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				repaint();
			}
		});
	}
}
