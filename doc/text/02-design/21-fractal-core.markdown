### Kernstück ###

Dieser Abschnitt beschreibt das Kernstück der Fraktalberechnung, die Klasse
`ComplexEscapeFractal`. Sobald das GUI kreiert und ersichtlich ist, wird durch 
eine Initialisierungs-Methode ein als standard gewähltes Fraktal gezeichnet. Dieses 
Standard-Fraktal ist in unserem Fall das Mandelbrot-Fraktal. Anhand der als standard 
definierten Werten wie Mandelbrot als Funktion und Grayscale als Farb-Schema 
wird beim Starten der Software dieses Fraktal gezeichnet. Die diversen 
Teilschritte sind wie folgt beschrieben:

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

~~~~~~~~ {.Java}
	/**
	 * Draws the fractal with a max number of iterations per point
	 * @param maxIterations the max number of iterations
	 */
	public void drawFractal(int maxIterations)
	{
		this.maxIterations = maxIterations;
		Thread thread = new Thread() {
			@Override
			public void run()
			{
				long start = System.currentTimeMillis();

				doDrawFractal();

				drawTime = System.currentTimeMillis() - start;

				if (statObserver != null)
					statObserver.statisticsDataAvailable();
			}
		};
		thread.start();
	}
~~~~~~~~

D.h. also dass die eigentliche Berechnung inkl. das Zeichnen der einzelnen Pixel in 
der Methode `doDrawFractal()` vorgenommen wird.

#### Schrittfunktion ####
In der oben genannten Methode `doDrawFractal()` werden in mehreren Loops die Brechnungen 
der einzelnen Pixel vorgenommen. Die eigentliche Iterationsvorschrift Z.B. wie bei 
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
	private void doDrawFractal()
	{
		stepCount = 0;

		int width = display.getImageWidth();
		int height = display.getImageHeight();

		scaler.setDimension(width, height);

		BufferedImage img = display.createImage();
		Graphics2D g = img.createGraphics();

		// the only two, reusable complex numbers
		ComplexNumber z0 = new ComplexNumber(0, 0);
		ComplexNumber z = new ComplexNumber(0, 0);

		/*
		 * main loop for all pixels
		 *
		 * loop hierarchy:
		 * - from coarse to fine
		 * -- rows
		 * --- pixels
		 */
		for (int step = 16, oldStep = 32; step > 0; oldStep = step, step /= 2) {
			for (int y = 0; y < height; y += step) {
				int start = 0;
				int inc = step;
				// skip pixel already drawn in last iteration
				if (step < 16 && y % oldStep == 0) {
					start = step;
					inc = oldStep;
				}

				double fractalY = scaler.scaleY(y);

				// inner loop: pixels
				for (int x = start; x < width; x += inc) {
					double fractalX = scaler.scaleX(x);

					// set the z0 and the variable z to the current values
					z0.set(fractalX, fractalY);
					z.set(fractalX, fractalY);

					// get iteration count using the step() function
					int count = 0;
					while (z.absSqr() < boundarySqr && count++ < maxIterations)
						function.step(z0, z);

					stepCount += count;

					// map to color and draw pixel
					Color color = getColor(count);
					g.setColor(color);
			        g.fillRect(x, y, step, step);
				}
			}
			display.updateImage(img, 0);
		}
	}
~~~~~~~~

#### Zeichnen der Orbits ####
Ist im GUI der Radio-Button Draw Path angewählt, so wird die Zoom-Funktion 
deaktiviert und das Zeichnen der Orbits (Pfad) aktiviert. Nach jedem Schritt 
wird in Bildschirmkoordinaten zurückgerechnet und eine dünne Linie gezeichnet. 
Die ersten 30 Schritte sind animiert, danach wird aufgrund der langen Wartezeiten 
der Rest des Pfades auf einmal gezeichnet.

~~~~~~~~ {.Java}
	private void doDrawOrbit()
	{
		ComplexNumber z0 = new ComplexNumber(
			scaler.scaleX(orbitX), scaler.scaleY(orbitY));
		ComplexNumber z = z0.clone();

		BufferedImage img = display.createImage();
		Graphics2D g = img.createGraphics();

		g.setStroke(new BasicStroke(0.4f));
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(orbitColor);

		int lastX = orbitX;
		int lastY = orbitY;

		int count = 0;
		while (z.absSqr() < boundarySqr && count++ < maxIterations) {
			function.step(z0, z);

			int x = scaler.unscaleX(z.getReal());
			int y = scaler.unscaleY(z.getImaginary());

			g.drawLine(lastX, lastY, x, y);

			lastX = x;
			lastY = y;

			// only show the first 30 steps "animated", the rest in one go
			if (count < 30 && orbitDelay > 0) {
				display.updateImage(img, 1);
				sleep(orbitDelay);
			}
		}
		display.updateImage(img, 1);
	}
~~~~~~~~

#### Zeichnen mittels JAVA 2D API ####
* TODO

