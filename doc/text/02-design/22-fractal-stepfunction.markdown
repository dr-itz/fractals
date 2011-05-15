### Die Schrittfunktion ###

Die eigentliche Funktion für einen einzelnen Schritt bei der Berechnung eines
Fraktals sind getrennt vom Kern implementiert. Dabei wird nur das Interface 
`StepFractalFunction` vorgegeben. Dieses definiert die Methoden die vom Kern
aufgerufen werden. Dadurch ist es möglich, die Funktionen einfach auszutauschen
und sehr einfach neue hinzuzufügen.

#### Das Interface ####

~~~~~~~~ {.Java}
public interface StepFractalFunction
{
	/**
	 * Returns the lower bounds of the fractal function
	 * @return lower bounds as a complex number
	 */
	ComplexNumber getLowerBounds();

	/**
	 * Returns the upper bounds of the fractal function
	 * @return upper bounds as a complex number
	 */
	ComplexNumber getUpperBounds();

	/**
	 * Returns the square of the absolute value of the boundary
	 * @return square of boundary
	 */
	double getBoundarySqr();

	/**
	 * Calculates a single step
	 * @param start the starting number (z0)
	 * @param var initially the last number (z_n), updated by the function to be
	 * 		the next number z_{n+1}. Since this is mutable it is trickier to
	 * 		handle for certain operations.
	 */
	void step(ComplexNumber start, ComplexNumber var);
}
~~~~~~~~

Die folgenden Funktionen müssen implementiert sein:

  * **`getLowerBounds()`**

	Diese Funktion gibt die untere sichtbare Begenzung als komplexe Zahl zurück.

  * **`getUpperBounds()`**

	Diese Funktion gibt die obere sichtbare Begenzung als komplexe Zahl zurück.

  * **`getBoundarySqaure()`**

	Diese Funktion gibt das Quadrat des absuluten Wert der komplexen Zahl
	zurück, welche die Begrenzung darstellt. Für Mandelbrot ist diese Bedingung
	$|z| < 2$, also muss diese Funktion $2^2 = 4$ zurückgeben.

  * **`getBoundarySqaure()`**

	Diese Funktion führt den eigentlichen Rechenschritt für
	$z_{n+1} = f(z_n, z_0)$ aus. Der übergebene Startwert darf nicht verändert
	werden, die variable ComplexNumber hingegen muss verändert werden.

`getLowerBound()` und `getUpperBounds()` liessen sich auch aus dem Rückgabewert
von `getBoundarySquare()` errechnen, dies wurde aber absichtlich nicht gemacht,
um die visuelle Anzeige flexibler zu gestallten.


#### Die Factory ####

Die Schrittfunktionen folgen dem Factory-Pattern. Dabei ist eine Factory die
zentralle Stelle um Instanzen der Schrittfunktion zu erhalten. Die Factory ist
die einzige Stelle, welche die einzelnen Implementation kennt. Das GUI fragt die
Factory nach einer Liste aller vorhanden Funktionen, wählt der Benutzer eine
aus, wird wieder die Factory nach einer Instanz der Funktion gefragt. Neue
Funktionen zu registrieren geschieht also nur in der Factory.


#### Hinzufügen einer neuen Funktion ####

Eine Anforderung an das Design war, neue Funktionen sehr einfach hinzufügen zu
können. Dieses Ziel wird erreicht durch das Oben beschriebene Interface sowie
der Factory. Als kleines Beispiel wird hier gezeigt wie eine Mandelbrot-Funktion
mit der folgenden Form hinzugefügt wird:

$$z_{n+1} = z_n^4 + z_0$$

Diese Funktion unterscheidet sich nur minimal von der normalen
Mandelbrot-Funktioen $z_{n+1} = z_n^2 + z_0$. Für die Implementation wird nicht
auf die bereits vorhanden Klasse `Mandelbrot` zurückgegriffen, die abgeleitet
werden könnte. Die neue Klasse soll `Mandelbrot4` heissen.

Zuerst wird die Implementation erstellt. Auf die Kommentare wird hier der
Übersichtlichkeit halber verzichtet;

~~~~~~~~ {.Java}
// src/main/java/ch/sfdr/fractals/fractals/Mandelbrot4.java
package ch.sfdr.fractals.fractals;

import ch.sfdr.fractals.math.ComplexNumber;

public class Mandelbrot4
	implements StepFractalFunction
{
	@Override
	public ComplexNumber getLowerBounds()
	{
		return new ComplexNumber(-2, -2);
	}

	@Override
	public ComplexNumber getUpperBounds()
	{
		return new ComplexNumber(2, 2);
	}

	@Override
	public double getBoundarySqr()
	{
		return 4;
	}

	@Override
	public void step(ComplexNumber start, ComplexNumber var)
	{
		var.pow(4).add(start);
	}
}
~~~~~~~~

Um die neue Funktion dem Rest der Software bekannt zu machen, muss diese nur
noch in der Factory registriert werden. Dabei muss eine einzelne Zeile in der
Klasse FractalFactory in das Array FUNCTIONS eingefügt werden:


~~~~~~~~ {.Java}
	get("Mandelbrot ^4", Mandelbrot4.class),
~~~~~~~~

Dies sieht dann z.B. so aus:

~~~~~~~~ {.Java}
	/**
	 * a list of all available functions
	 */
	private static Pair[] FUNCTIONS = {
		get("Mandelbrot", Mandelbrot.class),
		get("Mandelbrot ^4", Mandelbrot4.class),
	};
~~~~~~~~


