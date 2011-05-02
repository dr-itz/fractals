package ch.sfdr.fractals.gui.component;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * Display image interaction, the link between the core and the GUI.
 */
public interface ImageDisplay
{
	/**
	 * returns the width of the image area
	 * @return width
	 */
	int getImageWidth();

	/**
	 * returns the height of the image area
	 * @return height
	 */
	int getImageHeight();

	/**
	 * Creates a buffered image to draw on. The created Buffered is with RGBA.
	 * @return BufferedImage
	 */
	BufferedImage createImage();

	/**
	 * updates the displayed image with the supplied one. Must be thread safe
	 * @param img
	 */
	void updateImage(Image img);
}
