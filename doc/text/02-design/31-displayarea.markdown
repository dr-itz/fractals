### Die Komponente LayeredImage ###

Die Komponente `LayeredImage` ist eine nicht-visuelle Komponente die ein Bild
mit verschiedenen Layern darstellt. Sie implementiert das Interface `ImageDisplay`
und ist die Komponente auf der die Fraktale gezeichnet werden. Dabei sind folgende
Features implementiert:

* Layers
	* Erstellen von Layers
	* Löschen von Layers
	* Zeichnen auf jeden beliebigen Layer
* Speichern als Bild


### Die Anzeigekomponente DisplayArea ###

Die Anzeigekomponente `DisplayArea` ist die visuelle Komponente auf der die
Fraktale angezeigt werden. Ein `LayeredImage` enthält dabei die Bilddaten.
Folgende Features sind implementiert:

* Zugriff auf das `LayeredImage`
* Selektion mit halb-transparentem Rahmen für Zoom
* Listener für die Selektion

