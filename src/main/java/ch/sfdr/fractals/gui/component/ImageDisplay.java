package ch.sfdr.fractals.gui.component;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	 * @return number of layers
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

	/**
	 * Returns the BufferedImage of the layer itself. Use for direct
	 * manipulation of the image.
	 * @param layer index of the layer
	 * @return BufferedImage
	 */
	BufferedImage getLayerImage(int layer);

	/**
	 * Returns the Graphics2D of the layer itself. Use for direct
	 * manipulation of the image/graphics.
	 * @param layer index of the layer
	 * @return Graphics2D
	 */
	Graphics2D getLayerGraphics(int layer);

	/**
	 * updates a directly manipulated layer, ie. forces a redraw of the layers.
	 * @param layer index of the layer
	 */
	void updateLayer(int layer);


	/**
	 * save the current display to a file
	 * @param file the file to save to
	 * @param format the format
	 * @throws IOException
	 */
	void saveImage(File file, String format)
		throws IOException;
}
