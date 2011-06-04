### Kernstück ###

Dieser Abschnitt beschreibt das Kernstück der Fraktalberechnung, die Klasse
`ComplexEscapeFractal`. Diese Klasse übernimmt die Hauptaufgaben wie 
das Ausführen der Schrittfunktion und das Zeichnen der Fraktale oder der Orbits.


#### Initialisierung ####

In der GUI-Klasse `MainFrame` existiert eine `initialize()` Methode, welche als aller 
ersten Schritt ein Fraktal mit Standard-Werten wie Mandelbrot und Grayscale auf der 
`DisplayArea` zeichnet. Hierfür wird dem Konstruktor der `ComplexEscapeFractal`-Klasse 
diverse Parameter übergeben.

~~~~~~~~ {.Java}
	/**
	 * Creates the ComplexEscapeFractal
	 * @param display the display area to draw on
	 * @param scaler the scaler to convert between screen and fractal coordinates
	 * @param function the function to use
	 */
	public ComplexEscapeFractal(ImageDisplay display, Scaler scaler,
			StepFractalFunction function, ColorMap colorMap)
	{
		this.display = display;
		this.scaler = scaler;
		this.colorMap = colorMap;
		setFractalFunction(function);
	}
~~~~~~~~

Mit Hilfe dieser Parameter wird folgendes gesetzt:

* `ImageDisplay` für das zeichnen der Fraktale mittels Java 2D API
* `Scaler` für die Umrechnung von Bildschirmkoordinaten in skalierte 
  Koordinaten
* `StepFractalFunction` als Schrittfunktion zur Berechnung des Fraktals
* `ColorMap` als Farb-Schema für das Fraktal

Zusätzlich wird direkt mittels `setFractalFunction()` Methode die Schrittfunktion gesetzt.


#### Verwendung des Scalers ####

Der Scaler wird für die Umrechnung von Bildschirmkoordinaten in skalierte Koordinaten 
verwendet. In der `setFractalFunction()` wird dieser mit Hilfe seiner eigenen `init()` 
Methode initialisiert und so den Bereich festgelegt, in welcher Beschränkung sich die 
Schrittfunktion befindet. Bei Mandelbrot wäre dies die Beschränkung $|z| < 2$.


#### Berechnung im eigenen Thread ####

Damit die Berechnung des Fraktals die Anzeige des GUIs nicht blockiert, wird diese in 
einem eigenen Thread vollzogen.


#### Schrittfunktion ####

In der oben genannten Methode `doDrawFractal()` werden in mehreren Loops die Berechnungen 
der einzelnen Pixel vorgenommen. Die eigentliche Iterationsvorschrift z.B. wie bei 
Mandelbrot $z_{n+1} = z_n^2 + z_0$ wird mit der Methode `step()` durchgeführt und berechnet.


#### Farbauswahl ####

Mittels dem in der `ColorMapFactory` definierten Farb-Schema werden den Iterationen 
entsprechend die Pixel farbig gemalt.


#### Verwendung von Statistik-Daten ####

Das Interface `StatisticsObserver` wird verwendet um dem GUI über Vorhandensein neuer 
Statistik-Daten zu informieren und um diese dann anzeigen zu können. Hier handelt es 
sich um Statistik-Daten wie x-facher Zoom, Anzahl Aufrufe der Schrittfunktion resp. 
der Methode `step()` und die Zeit die benötigt wurde um das Fraktal zu zeichnen.

~~~~~~~~ {.Java}
public interface StatisticsObserver
{
	/**
	 * Method is called when new statistic data is available
	 */
	void statisticsDataAvailable();
}
~~~~~~~~


#### Zeichnen des Fraktals ####

Für jeden Bildpunkt werden die Anzahl Schritte gezählt bis max. Iterationen erreicht sind, 
oder die Begrenzung überschritten wird. Mit der `step()` Methode des zu berechnenden 
Fraktals wird die Berechnung für die Farbzuweisung der einzelnen Bildpunkte vorgenommen. 
Die effektive Farbzuweisungen aus dem gewählten Farbschema wird in einem verschachtelten Loop 
vorgenommen.

~~~~~~~~ {.Java}
ComplexNumber z0 = new ComplexNumber(0, 0);
ComplexNumber z = new ComplexNumber(0, 0);

for (int y = 0; y < height; y++) {
	int start = 0;
	double fractalY = scaler.scaleY(y);

	// inner loop: pixels
	for (int x = start; x < width; x++) {
		double fractalX = scaler.scaleX(x);

		// set the z0 and the variable z to the current values
		z0.set(fractalX, fractalY);
		z.set(fractalX, fractalY);

		// get iteration count using the step() function
		int count = 0;
		while (z.absSqr() < boundarySqr && count++ < maxIterations)
			function.step(z0, z);

		// map to color and draw pixel
		Color color = getColor(count);
		g.setColor(color);
		g.fillRect(x, y, 1, 1);
	}
}
display.updateImage(img, 0);
~~~~~~~~


#### Zeichnen der Orbits ####

Ist im GUI der Radio-Button "Orbit drawing" angewählt, so wird die Zoom-Funktion 
deaktiviert und das Zeichnen der Orbits aktiviert. Nach jedem Schritt 
wird in Bildschirmkoordinaten zurückgerechnet und eine dünne Linie gezeichnet. 
Die ersten 30 Schritte sind animiert, danach wird aufgrund der langen Wartezeiten 
der Rest des Orbits auf einmal gezeichnet.

~~~~~~~~ {.Java}
LineClipping lc = new LineClipping();
double lastX = orbitX;
double lastY = orbitY;

int count = 0;
while (z.absSqr() < boundarySqr && count++ < maxIterations) {
	function.step(z0, z);

	double x = scaler.unscaleX(z.getReal());
	double y = scaler.unscaleY(z.getImaginary());

	if (lc.clipLineToRectangle(lastX, lastY, x, y, 0, 0, width, height)) {
		g.drawLine(lc.getClipX1(), lc.getClipY1(),
			lc.getClipX2(), lc.getClipY2());

		if (count < 30 && orbitDelay > 0) {
			display.updateImage(img, 1);
			sleep(orbitDelay);
		}
	}

	lastX = x;
	lastY = y;
}
~~~~~~~~

Die X- und Y-Koordinaten werden zuerst als double-Werte errechnet.  Der
Hintergrund hierfür ist, dass bei sehr starkem Zoom ein 32-bit Integer nicht
ausreicht um die Koordinaten darzustellen. Zwar gibt die Auflösung des
Bildschirms die möglichen Koordinaten vor, jedoch liegen bei hohem Zoom die
meisten Endpunkte der Linien ausserhalb dises Bereiches. Die Java 2D API kann
mit Punkten im nicht sichtbaren Bereich umgehen und die korrekten Linien
zeichnen, jedoch ist die API auf 32-bit Integer beschränkt. Wird einfach auf
Integer gecastet, führt dies zu falschen Werten, Linien ändern plötzlich die
Richtung. Um dieses Problem zu lösen, wurde ein Line-Clipping eingeführt.
Line-Clipping errechnet anhand von zwei Endpunkten und einem Clipping-Bereich
(sichtbarer Bereich) den sichtbaren Bereich einer Linie und dessen entsprechende
Endpunkte im sichtbaren Bereich. Der Algorithmus der hier verwendet wird, ist
"Cohen-Sutherland line clipping".  Weitere mögliche und zum Teil auch schnellere
Algorithmen hierfür sind:

* Nicholl–Lee–Nicholl
* Liang–Barsky
* Cyrus–Beck

Cohen-Sutherland bietet den Vorteil sehr effizient entscheiden zu können, ob
eine Linie überhaupt im Sichtbaren Bereich liegt und umgerechnet werden muss.
Ein weiterer Vorteil ist, dass er bekannt und gut dokumentiert ist. Der hier
verwendete Code ist von dem C++ Beispiel von Wikipedia nach Java übersetzt und
leicht angepasst worden. Die Effizienz ist für diese Anwendung hier mehr als
ausreichend, weshalb andere Algorihmen nicht mehr näher betrachtet wurden.


