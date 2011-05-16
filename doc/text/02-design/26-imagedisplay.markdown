### Das Interface ImageDisplay ###

Das Interface `ImageDisplay` stellt die Funktionen zur Verfügung, die benötigt werden, um
BufferedImage bzw. Graphics2D Objekte der Java 2D API zu erhalten und um diese
danach auf dem GUI anzeigen zu können. Es wurde entschieden, dies als Interface
zu Implementierten, da die Anzeige auf dem GUI nicht die einzige Möglichkeit
darstellt. Eine weitere Mögliche Anwendung ist das Speichern des gezeichneten
Fraktals als Bild. Dabei wird natürlich die Bildschirmdarstellung irrelavant und
somit macht es Sinn nur die Schnittstelle zu definieren.

Das Interface ist ausgelegt für das Zeichnen von Bildern in mehreren Layern. Dies
wird zum Besispiel beim Anzeigen der Orbits (Pfad) verwendet.

Das Interace:

~~~~~~~~ {.Java}
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
~~~~~~~~

Die wichtigsten Funktionen:

  * **`getImageWidth()`** und **`getImageHeight()`**

	Geben die aktuelle Grösse des angezeigten Bildes zurück.

  * **`createImage()`**

	Erstellt ein BufferedImage der Java 2D API. Auf dieses kann mittels eines
	Graphics2D-Objektes gezeichnet werden. Das erstellte BufferedImage hat
	bereits die richtige Grösse und ist am Anfang einfach transparent.

  * **`updateImage()`**

	Zeichnet das angegebene BufferedImage auf den angegebenen Layer.

  * **`clearLayer()`**

	Löscht den Inhalt des angegebenen Layers und macht ihn wieder komplett
	transparent.


Die Implementation dieses Interfaces ist in der Klasse `DisplayArea` im Kapitel zum GUI näher
beschrieben.
