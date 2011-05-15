package ch.sfdr.fractals.gui.component;

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
	 * returns the number of layers
	 * @return
	 */
	int getLayers();

	/**
	 * adds a layer and returns it's index
	 * @return layer index of the layer
	 */
	int addLayer();

	/**
	 * removes a layer
	 * @param layer index of the layer
	 */
	void removeLayer(int layer);

	/**
	 * clears the contents of a layer, resetting it to transparent
	 * @param layer index of the layer
	 */
	void clearLayer(int layer);


	/**
	 * updates the displayed image with the supplied one. Must be thread safe
	 * @param img
	 */
	void updateImage(BufferedImage img, int layer);
}
