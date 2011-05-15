### Die Schnittsetlle ImageDisplay ###

Die Schnittstelle stellt die Funktionen zur Verfügung, die benötigt werden um
BufferedImage bzw. Graphics2D Objekte der Java 2D API zu erhalten, und diese
danach auf dem GUI anzeigen zu können. Es wurde entschieden, dies als Interface
zu Implementierten, da die Anzeige auf dem GUI nicht die einzige Möglichkeit
darstellt. Eine weitere Mögliche Anwendung ist das Speichern des gezeichneten
Fraktals als Bild. Dabei wird natürlich die Bildschirmdarstellung irrelavant und
somit macht es Sinn nur die Schnittstelle zu definieren.

Das Interface ist ausgelegt für das Zeichnen von Bilder in mehreren Layern. Dies
wird zum Besispiel bein Anzeigen der Orbits verwendet.

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
	Graphics2D-Ojektes gezeichnet werden. Das erstellete BufferedImage hat
	bereits die richtige Grösse und ist am Anfang einfach transparent.

  * **`updateImage()`**

	Zeichnet das angegebene BufferedImage auf den angegebenen Layer.

  * **`clearLayer()`**

	Löscht den Inhalt des angegebenen Layer und macht ihn wieder komplett
	transparent


Die Implementation des Interface, die `DisplayArea` ist im Kapitel zum GUI näher
beschrieben.
