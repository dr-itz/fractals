## Erweiterung: Finden von Zyklen ##

Eine der möglichen Erweiterungen die umgesetzt wurden, ist das Finden von
zyklischen Orbits, implementiert in der Klasse `ComplexOrbitCycleFinder`.  Dabei
geht es darum, Orbits zu finden welche sich zyklisch verhalten, d.h. nach einer
bestimmten Anzahl Schritten wieder am Ausgangspunkt sind. Für Mandelbrot z.B.
finden sich alle Zyklen der Länge drei durch das Lösen der folgenden Gleichung:
$$z = ((z^2 + z)^2 + z)^2 + z$$

Im Vergleich dazu finden sich Zyklen der Länge drei in der Julia-Menge durch das
Lösen der folgenden Gleichung:
$$z = ((z^2 + c)^2 + c)^2 + c$$


Um die Gleichungen zu lösen, werden diese als Nullstellen-Problem betrachtet:
$$F(z) = 0$$


Beispielsweise für Mandelbrot:
$$F(z) = ((z^2 + z)^2 + z)^2 = 0$$


Diese werden mittels des Newton-Verfahren in zwei Dimensionen gelöst. Die
Iterationsvorschrift hierzu lautet:
$$z_{n+1} = z_n - J(z_n)^{-1} \cdot F(z)$$


$J(z_n)$ ist dabei die Jacobi-Matrix an der Stelle $z_n$. Die komplexen
Zahlen setzen sich aus zwei Teilen zusammen, dem rellen und dem imaginären.
Somit sind zwei Variabeln wie auch zwei Funktionen vorhanden. Die komplexen
Zahlen werden als Vektor betrachtet, die Funktion $F(z)$ als zwei Funktionen von
zwei Variabeln:

$$z = (x + yi) \rightarrow \begin{pmatrix} x \\ y \end{pmatrix}
\text{bzw.  }
F(z) = \begin{pmatrix} R(x, y) \\ I(x,y) \end{pmatrix}$$

Wobei $R(x, y)$ den Realteil der Funktion darstellt, $I(x,y)$ den Imaginärteil.
Somit kann die Iteration wie folgt geschrieben werden:

$$\displaystyle
\begin{pmatrix} x_{n+1} \\ y_{n+1} \end{pmatrix}
= 
\begin{pmatrix} x_n \\ y_n \end{pmatrix}
-
\begin{pmatrix}
	\displaystyle\frac{\partial R(x_n, y_n)}{\partial x} &
		\displaystyle\frac{\partial R(x_n, y_n)}{\partial y} \\
	\displaystyle\frac{\partial I(x_n, y_n)}{\partial x} &
		\displaystyle\frac{\partial I(x_n, y_n)}{\partial y}
\end{pmatrix}^{-1}
\cdot
\begin{pmatrix} R(x_n, y_n)) \\ I(x_n, y_n)) \end{pmatrix}$$


Da die hier verwendete Jacboi-Matrix nur 2x2 gross ist, ist die Berechnung der
Inversen besonders einfach:

$$A = \begin{pmatrix} a_{11} & a_{12} \\ a_{21} & a_{22} \end{pmatrix}
\rightarrow A^{-1} = 
\frac{1}{a_11 \cdot a_{22} - a_{12} \cdot a_{21}} \cdot
\begin{pmatrix} a_{22} & -a_{12} \\ -a_{21} & a_{11} \end{pmatrix}$$

Im Java-Code wurde dazu eine innere Klasse zu `ComplexOrbitCycleFinder`
implementiert. Diese Klasse trägt den Namen `Jacobian2x2` und stellt eine 2x2
Jacobi-Matrix dar, die sich selbst invertierten kann:

~~~~~~~~ {.Java}
public void invert()
{
	double det = elem[0][0] * elem[1][1] - elem[0][1] * elem[1][0];
	inv[0][0] =  elem[1][1] / det;
	inv[0][1] = -elem[0][1] / det;
	inv[1][0] = -elem[1][0] / det;
	inv[1][1] =  elem[0][0] / det;
}
~~~~~~~~

Die Funktion $F(z)$ bzw. $F(x,y)$ wird gebildet durch n-faches Aufrufen der
Schrittfunktion, wobei "n" die gesuchte Zykluslänge darstellt. Um das Resultat
in die richtige Form zu bringen wird noch $z_0$ subtrahiert. Der Anfangswert
$z_0$ ist dabei ein "geeigneter" Startwert.

~~~~~~~~ {.Java}
private ComplexNumber callFunction(ComplexNumber start, ComplexNumber res)
{
	res.set(start);
	for (int i = 0; i < cycleLength; i++)
		function.step(start, res);
	res = res.subtract(start);
	return res;
}
~~~~~~~~


Die nötigen partiellen Ableitungen für die Jacobi-Matrix berechnen sich rein
numerisch mittels des zentralen Differenzenquotienten:

$$D_2 f(x_0, h) = \frac{f(x_0 + h) - f(x_0 - h)}{2h}$$


Ebenfalls in der Klass `Jacobian2x2` ist die Funktion implementiert die die
Jacobi-Matrix aus den Funktionswerten berechnet:

~~~~~~~~ {.Java}
public Jacobian2x2 jacobian(ComplexNumber z)
{
	// x part
	start.set(z.getReal() + DIFF_H, z.getImaginary());
	callFunction(start, zd);
	start.set(z.getReal() - DIFF_H, z.getImaginary());
	callFunction(start, zd2);
	zd.subtract(zd2);

	set(0, 0, zd.getReal() / (2 * DIFF_H));
	set(1, 0, zd.getImaginary() / (2 * DIFF_H));

	// y part
	start.set(z.getReal(), z.getImaginary() + DIFF_H);
	callFunction(start, zd);
	start.set(z.getReal(), z.getImaginary() - DIFF_H);
	callFunction(start, zd2);
	zd.subtract(zd2);

	set(0, 1, zd.getReal() / (2 * DIFF_H));
	set(1, 1, zd.getImaginary() / (2 * DIFF_H));

	return this;
}
~~~~~~~~

Die Newton-Iteration selbst sieht dann wie folgt aus. Wieder wurde darauf
geachtet, dass in der Schleife der Iteration selbst keine neuen Objekte
alloziert werden.

~~~~~~~~ {.Java}
private ComplexNumber findCycle(int cycleLenght, ComplexNumber start)
{
	this.cycleLength = cycleLenght;

	// the value that should converge to the solution
	ComplexNumber z = start.clone();

	// the function value, i.e. the difference to 0
	ComplexNumber f = new ComplexNumber(1, 1);

	// the delta from the jacobian multiplied by the function value
	ComplexNumber delta = new ComplexNumber(0, 0);

	// the jacobian (and it's inverse)
	Jacobian2x2 jac = new Jacobian2x2();

	int i = 0;
	while ((Math.abs(f.getReal()) > TOL ||
			Math.abs(f.getImaginary()) > TOL) &&
			i++ < maxIter)
	{
		// get the inverse of the jacobian
		jac.jacobian(z).invert();

		// get the function value
		callFunction(z, f);

		// multiply inverse of the jacobian with the current function value
		delta.set(
			jac.get(0, 0) * f.getReal() +
				jac.get(0, 1) * f.getImaginary(),
			jac.get(1, 0) * f.getReal() +
				jac.get(1, 1) * f.getImaginary());

		// subtract the delta form the current value
		z.subtract(delta);
	}

	if (i >= maxIter || z.isNaN())
		return null;

	return z;
}
~~~~~~~~

![Klassendiagramm der ComplexOrbitCycleFinder](figures/cycle-finder.png)


### Finden von allen Zyklen einer bestimmten Länge ###

Da das Newton-Verfahren jeweils nur eine Lösung, gegeben durch den Startwert,
finden kann, müssen geeignete Startwerte gefunden werden, die zu allen Lösungen
führen. Die gewählte Methode funktioniert wie folgt:

* Der begrenzende Kreis wird in x- und y-Richtung in $Zykluslaenge^2$
  Schritte aufgeteilt. Dies ergibt ein Gitter, das über den Kreis gelegt wird.
* Die linke untere Ecke dieser Rechtecke bzw. Quadrate dient als Startwert für
  das Newton-Verfahren.
* Wird eine Lösung gefunden, wird diese mit allen vorher gefundenen Lösungen
  verglichen, um zu entscheiden, ob es sich um eine neue Lösung handelt. Dabei
  werden die Real- und Imaginärteile verglichen. Liegen beide innerhalb einer
  gewissen Toleranz, gilt die Lösung als bereits bekannt und wird verworfen.

Für Mandelbrot und eine Zykluslänge von 4 ergibt sich also ein 16x16-Raster das
über den begrenzenden Kreis gelegt wird. Dies ergibt ca. 195 Punkte, die als
Startwerte durchgerechnet werden. Die restlichen 61 liegen ausserhalb des
Kreises und werden somit nicht berücksichtigt.


~~~~~~~~ {.Java}
...
for (double x = xmin; x < xmax; x += stepSizeX) {
	for (double y = ymin; y < ymax; y += stepSizeY) {
		// skip points outside the boundary circle
		if (x * x + y * y > boundary)
			continue;

		start.set(x, y);
		ComplexNumber z = findCycle(cycleLength, start);

		...

		// check list if this (very similar) point has already been found
		boolean newCycle = addNewCycle(z);
		if (newCycle)
			listener.cycleFound(z, cycleLength);
	}
}
...
~~~~~~~~

