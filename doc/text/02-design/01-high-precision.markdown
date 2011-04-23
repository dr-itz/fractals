## Hohe Präzision beim Rechnen ##

Eine der möglichen Erweiterungen ist das Benutzen von Arbitrary Precision Math,
also das Rechnen mit beliebiger Präzision um die Grenzen des normalen "double"
zu erweitern. Da diese Art des Rechnen aufwändiger ist, wurden vorgängig
Messungen durchgeführt, um entscheiden zu können, ob diese Erweiterung beim
Design der Software überhaupt berücksichtigt werden soll.

Um diese Messungen durchführen zu können, wurden minimale Implementationen des
Mandelbrot-Fraktals vorgenommen, ohne Zoom oder andere Features. Eine
Java-Library die Arbitrary Precision Math implementiert ist "Apfloat", diese
gilt als sehr performant. Ein anderer Ansatz, höhere Präzision zu erreichen ist
das Rechnen mit zwei statt nur einem "double" pro Zahl. Die Algorithmen hierzu
sind von Knuth, Kahan, Dekker und Linnainmaa beschrieben und von Martin Davis
in der Java-Klasse "DoubleDouble" implementiert.


### Implementationen ###

#### Normaler "double" ####

Zuerst wurde eine Klasse "ComplexNumber" implementiert mit der minimal
notwendigen Funktionalität:

~~~~~~~~ {.java}
public class ComplexNumber
{
	private double real;
	private double imaginary;

	public ComplexNumber(double real, double imaginary)
	{
		this.real = real;
		this.imaginary = imaginary;
	}
	public ComplexNumber add(ComplexNumber b)
	{
		return new ComplexNumber(real + b.real, imaginary + b.imaginary);
	}

	public ComplexNumber multiply(ComplexNumber m)
	{
		double c = m.real;
		double d = m.imaginary;
		return new ComplexNumber(real * c - imaginary * d, real * d + imaginary * c);
	}

	public double absSqr()
	{
		return real * real + imaginary * imaginary;
	}
}
~~~~~~~~

Die Funktion `absSqr()` berechnet dabei das Quadrat des Betrag, also
$real^2 + imaginary^2$. Die Wurzel zu ziehen würde nur unnötige Rechenzeit in
Anspruch nehmen.

Die Berechnung des Fraktal wird durch den folgenden Code durchgeführt:

~~~~~~~~ {.java}
double r = 1.0D / Math.min(width, height);
for (int y = 0; y < height; y++) {
	for (int x = 0; x < width; x++) {
		double fractalX = 4 * (x * r) - 2;
		double fractalY = 4 * (y * r) - 2;

		ComplexNumber z0 = new ComplexNumber(fractalX, fractalY);
		ComplexNumber z = z0;

		int count = 0;
		while (z.absSqr() < 4 && count++ < maxIter)
			z = z.sqr().add(z0);

		if (count >= maxIter)
			count = 0;

		Color color = getColor(count);
		g.setColor(color);
		g.fillRect(x, y, 1, 1);
	}
}
~~~~~~~~

Die Funktion `getColor()` weisst dabei einer Iterationszahl eine Farbe zu. Die
Ausgabe geschieht durch ein `Graphic`-Objekt, hier in der Variabel `g`. Die
Variabel `maxIter` legt dabei die maximale Anzahl Iterationen fest.


#### DoubleDouble ####

Analog zu der Implementation mit "double" wurde eine Implementation mit der
"DoubleDouble"-Klasse durchgeführt.

~~~~~~~~ {.java}
public class ComplexNumber
{
	private DoubleDouble real;
	private DoubleDouble imaginary;

	public ComplexNumber(DoubleDouble real, DoubleDouble imaginary)
	{
		this.real = real;
		this.imaginary = imaginary;
	}

	public ComplexNumber add(ComplexNumber b)
	{
		return new ComplexNumber(real.add(b.real), imaginary.add(b.imaginary));
	}

	public ComplexNumber multiply(ComplexNumber m)
	{
		DoubleDouble c = m.real;
		DoubleDouble d = m.imaginary;
		return new ComplexNumber(
			real.multiply(c).subtract(imaginary.multiply(d)),
			imaginary.multiply(c).add(real.multiply(d)));
	}

	public DoubleDouble absSqr()
	{
		return real.sqr().add(imaginary.sqr());
	}
}
~~~~~~~~

Die Berechnung des Fraktal wird durch den folgenden Code durchgeführt:

~~~~~~~~ {.java}
DoubleDouble r = new DoubleDouble(1.0D).divide(
	new DoubleDouble(Math.min(width, height)));
DoubleDouble range = new DoubleDouble(4.0);
DoubleDouble min = new DoubleDouble(-2.0);
DoubleDouble limit = new DoubleDouble(4.0);

for (int y = 0; y < height; y++) {
	for (int x = 0; x < width; x++) {
		DoubleDouble fractalX = range.multiply(
			new DoubleDouble(x).multiply(r).add(min);
		DoubleDouble fractalY = range.multiply(
			new DoubleDouble(y).multiply(r).add(min);

		ComplexNumber z0 = new ComplexNumber(fractalX, fractalY);
		ComplexNumber z = z0;

		int count = 0;
		while (z.absSqr().lt(limit) && count++ < maxIter)
			z = z.multiply(z).add(z0);

		if (count >= maxIter)
			count = 0;

		Color color = getColor(count);
		g.setColor(color);
		g.fillRect(x, y, 1, 1);
	}
}
~~~~~~~~

Der Unterschied zur Implementation mit "double" sind dabei vorallem die
Rechenoperationen, die nun Aufrufe von Methoden darstellen.


#### Apfloat ####

Die Apfloat-Library hat bereits eine Implementation von komplexen Zahlen,
`Apcomplex`. Diese wird direkt verwendet:

~~~~~~~~ {.java}
int DIGITS = 10;
Apfloat r = new Apfloat(1, DIGITS).divide(
	new Apfloat(Math.min(width, height), DIGITS));
Apfloat range = new Apfloat(4.0, DIGITS);
Apfloat min = new Apfloat(-2.0, DIGITS);
Apfloat limit = new Apfloat(2.0, DIGITS);

for (int y = 0; y < height; y++) {
	for (int x = 0; x < width; x++) {
		Apfloat fractalX = range.multiply(
			new Apfloat(x, DIGITS).multiply(r).add(min);
		Apfloat fractalY = range.multiply(
			new Apfloat(y, DIGITS).multiply(r).add(min);

		Apcomplex z0 = new Apcomplex(fractalX , fractalY);
		Apcomplex z = z0;

		int count = 0;
		while (ApcomplexMath.abs(z).compareTo(limit) < 0 && count++ < maxIter)
			z = z.multiply(z).add(z0)

		if (count >= maxIter)
			count = 0;

		Color color = getColor(count);
		g.setColor(color);
		g.fillRect(x, y, 1, 1);
	}
}
~~~~~~~~

Bei Apfloat muss bei jeder Zahl angegeben werden, wie präzise gerechnet werden
soll. Für das Beispiel hier wurde eine Präzision von 10 Stellen verwendet. Im
Vergleich dazu rechnet ein normaler "double" mit ca. 15 Stellen, ein
"DoubleDouble" mit ca. 30 Stellen. Der Grund dafür wird bei den
Performance-Vergleichen offensichtlich.


### Vergleich der Performance ###

Mit den oben vorgestellten Implementation wurden Tests durchgeführt. Dabei wurde
die Zeit in Millisekunden gemessen, die benötigt wird, um ein Mandelbrot-Fraktal
mit der Grösse 590x450 Pixel mit maximal 200 Iterationen pro Bildpunkt zu
berechnen. Die Werte sind Durchschnitte aus 5 Durchläufen. Alle Messungen wurden
mit Java 1.6.0\_20 auf einem Intel Core 2 Duo 6600 @2.4 GHz durchgeführt.


Implementation      Zeit       Faktor
--------------  --------    ---------
double             269ms          1.0
DoubleDouble      1654ms          6.1
Apfloat         141016ms        524.2
--------------  --------    ---------
: Messresultate der verschiedenen Implementationen


Die Werte zeigen auf, dass Apfloat mit nur 10 Stellen Genauigkeit 524x langsamer
als ein normaler "double" ist. Aus diesem Grund wird der Ansatz mit Arbitrary
Precision mittels Apfloat nicht weiter verfolgt.

DoubleDouble mit ca. 30 Stellen Genauigkeit ist jedoch nur 6x langsamer. Auch
dies ist ein grosser Unterschied und da höhere Präzision nur bei sehr hohem Zoom
einen Unterschied macht, wird anfangs nicht mit DoubleDouble gearbeitet.
