## Testen der Software ##

### Automatische Tests mit JUnit ###

Alle Klassen die nicht mit dem GUI direkt zusammenhängen, sprich nicht von einer
AWT oder Swing-Klasse abhängen, lassen sich mittels JUnit testen. Dabei wird auf
eine möglichst hohe Abdeckung geachtet. Dies wird mittels des Coverage-Tool
Cobertura gemessen.

Für die folgenden Klassen sind Tests mittels JUnit implementiert:

* ComplexNumber
* Scaler
* Mandelbrot und Mandelbrot3
* Julia und Julia3
* FractalFactory
* ColorMap
* ColorMapFactory
* ColorSelection
* LineClipping
* ComplexOrbitCycleFinder


### Manuelle Tests des GUI ###

Klassen die nicht automatisch getestet werden können, oder die Implementierung
der Tests viel zu aufwändig wäre, werden von Hand getestet. Dies betrifft das
GUI. Folgende Tests werden manuell durchgeführt:

  * **Start**

	Nach dem Starten ist ein Mandelbrot-Fraktal in Graustufen gezeichnet. Zoom
	wird als "1x" angezeigt.

  * **Zoom**

	Mit der Maus wird ein Bereich des Fraktals selektiert. Die Selektion
	erscheint dabei halb-transparent. Der selektierte Bereich wird vergrössert
	dargestellt, der Zoom wird entsprechend angezeigt. Mit der rechten Maustaste
	lässt sich der Zoom verringern (um den Faktor 3).

  * **Anzeige der Koordinaten**

	Wird mit der Maus über das Fraktal gefahren, werden im Info-Panel die
	Koordinaten als komplexe Zahl dargestellt.

  * **Anzeige des Fortschritts**

	Während das Fraktal aufgebaut wird, erscheint unterhalb von "Steps
	calculated" eine Progress-Bar die den Fortschritt anzeigt. Um dies testen zu
	können muss das Fenster gross genug sein, damit die Berechnung lange genug
	dauert um die Progress-Bar zu sehen.

  * **Von grob zu fein**

	Wird das Fraktal gezeichnet, wird von grob zu fein gezeichnet, d.h. es
	erscheint zuerst ein grobes, verpixeltes Bild das immer feiner wird.

  * **Auswahl der Fraktal-Funktion**

	Als Fraktalfunktion wird "Julia" ausgewählt. Es wird ein Julia-Fraktal
	gezeichnet. Neu eingeblendet werden die Felder "Const Real/Imag" die es
	erlauben die Konstante der Julia-Funktion zu definieren. Diese wird
	verändert, durch klicken auf "Draw" zeichnet sich das Fraktal neu. Zum
	Vergleich können Beispiele von Wikipedia zu Hilfe genommen werden.

  * **Farbauswahl**

	Das Farbschema wird von "Grayscale" auf "Blue" umgestellt. Das Fraktal
	wird in Blautönen neu gezeichnet. Die "Set color" wird auf "Red" umgestellt,
	die Menge selbst wird rot gezeichnet.

  * **Max. Iterationen**

	Es wird in das Mandelbrot-Fraktal am Rande der Menge hineingezoomt. Nun wird
	"Max. iterations" von 200 auf 300 erhöht. Es werden mehr Details gezeichnet.

  * **Verändern der Fenstergrösse**

	Die Grösse des Fensters wird verändert. Das Fraktal passt sich der neuen
	Fenstergrösse an. Dabei bleibt der sichtbare Bereich unverändert und wird
	lediglich entsprechend skaliert.

  * **Zeichnen von Orbits**

	Die "Click action" wird auf "Orbit drawing" umgestellt. Im Fraktal wird ein
	Punkt angeklickt, es zeichnet sich ein Orbit. Die ersten Schritte (30) sind
	animiert dargestellt. Die Geschwindigkeit lässt sich über die Einstellung
	"Step delay(ms)" verändern. Die Orbits werden in der ausgewählten Farbe
	gezeichnet. Ist die Checkbox "Auto-cycle" angewählt, wird automatisch nach
	jedem Orbit die Farbe umgestellt. Ist die Checkbox nicht ausgewählt, werden
	alle Orbits mit der selben Farbe gezeichnet.

  * **Löschen von Orbits**

	Es werden einige Orbits gezeichnet. Die Schaltfläche "Clear orbits" wird
	angeklickt, alle Orbits verschwinden.

  * **Zoom mit Orbits**

	Es werden einige Orbits gezeichnet. Nun wird umgeschaltet auf "Zoom" und in
	einen Bereich mit einem Orbit hineingezoomt. Die Linien und Startpunkte
	bleiben erhalten und zeichnen sich an der richtigen Stelle.

  * **Zeichnen eines Orbits mit Eingabe des Startpunktes**

	Es wird ein Orbit gezeichnet. Der Startpunkt wird in die Felder "Manual
	Real/Imag" übernommen. Nun werden diese Werte leicht verändert. Die
	Schaltfläche "Draw orbit" wird angeklickt, der Orbit wird gezeichnet. Dabei
	werden auch die Einstellungen der Frabe, "Auto-cycle" und "Step deplay"
	berücksichtigt.

  * **Finden von Zkylen**

	Im Mandelbrot-Fraktal wird die "cycle length" auf 4 eingestellt, der "Delay"
	auf 500, "Full orbits" ist ausgewählt. Die Schaltfläche "Find" wird
	angeklickt. Während des Suchvorgang erscheint unterhalb von "Cycles found"
	eine Progress-Bar. Die einzelnen Orbits werden im Abstand von ca. einer
	halben Sekunde gezeichnet, beeinflussbar durch den "Delay". Nachdem die Suche
	abgeschlossen ist, wird dort "8 cycles of length 4" angezeigt. Im Fraktal
	finden sich 8 Startpunkte mit den entsprechenden Orbits. Zoom und "Clear
	orbits" funktionieren ebenfalls. Nach einem "Clear orbits" wird "Full
	orbits" abgeschaltet, die Suche wird nochmals gestartet. Im Unterschied zu
	vorher werden nun nur die 8 Startpunkte gezeichnet, jedoch nicht die Orbits
	selbst.

  * **Reset**

	Es wird in das Fraktal hineingezoomt und einige Orbits gezeichnet. Die
	Schaltfläche "Reset" wird angewählt. Der Zoom wird wieder auf "1x" gesetzt,
	alle Orbits werden gelöscht.

