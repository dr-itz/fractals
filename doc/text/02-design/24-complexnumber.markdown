### Die Implementation der komplexen Zahl ###

Die Klasse `ComplexNumber` implementiert die Repräsentation der komplexen Zahl die,
für die Fraktal-Berechnung verwendet wird. Die Klasse basiert auf zwei
double-Werten, die den reellen und den imaginären Teil der komplexen Zahl
darstellen. Die folgenden Operationen sind implementiert, zusäzliche
Operationen können nach Bedarf implementiert werden:

  * Addition

	$(a + bi) + (c + di) = (a + c) + (b + d) \cdot i$

  * Subtraktion

	$(a + bi) - (c + di) = (a - c) + (b - d) \cdot i$

  * Multiplikation mit reeller Zahl

	$(a + bi) \cdot n = (a \cdot n) + (b \cdot n \cdot i)$

  * Multiplikation mit komplexer Zahl

	$(a + bi) \cdot (c + di) = (a \cdot c - b \cdot d) + (b \cdot c + a \cdot d)
	\cdot i$

  * Division

	$\displaystyle \frac{a + bi}{c + di} = \left({ac + bd \over c^2 + d^2}\right) +
	\left( {bc - ad \over c^2 + d^2} \right) \cdot i$

  * Potenzieren mit integer Zahl, als Folge von Multiplikationen

	$\displaystyle (a + bi)^n = \prod_{k=1}^n (a + bi)$

  * Bestimmen des Betrags

	$\displaystyle |(a + bi)| = \sqrt{a^2 + b^2}$

  * Bestimmen des Quadrates des Betrags (aus Performance-Gründen)

	$\displaystyle |(a + bi)|^2 = a^2 + b^2$


#### Veränderbarkeit ####

Die Klasse wurde so ausgelegt, dass Operationen direkt die Werte des Objektes
verändern. Z.B. der folgende Code:

~~~~~~~~ {.Java}
ComplexNumber x = new ComplexNumber(1.0, 2.0);
ComplexNumber y = new ComplexNumber(2.0, 3.0);
x.add(y);
~~~~~~~~

Beim Aufruf `x.add(y)` wird direkt der Wert von `y` zu `x` addiert. Alle
Operationen die als Resultat wieder eine komplexe Zahl haben, geben eine
ComplexNumber zurück. Diese ist jedoch einfach wieder die gleiche Instanz.
Dieses Pattern erlaubt die einfache Verkettung von Operationen.

Der Grund für das direkte Modifizieren des Wertes ist die Peformance. Je nach
Einstellung der maximalen Anzahl Schritte, des Zooms und der Auflösung wird die
Schrittfunktion `step()` der Fraktalfunktion ohne Probleme mehr als 200
Millionen Mal aufgerufen.

Anfänglich wurde bei jeder Operation der ComplexNumber das Resultat als ein
neues Objekt zurück gegeben. Dies erlaubt einen sehr einfachen Umgang mit den
Berechnungen, da nicht auf ungewollte Veränderungen rücksicht genommen werden
muss. Die Auswertung der CPU-Auslastung bei einem Dual-Core-System und Resultate
von Profilen haben gezeigt, dass das Erstellen von neuen Objekten einen
Flaschenhals darstellt. 200 Millionen Aufrufe bedeuteten auch 200 Millionen
Objekte, die erstellt und auch gleich wieder verworfen wurden. Dies
führte zu einer massiven Verlangsamung: Eine CPU war zu ca. 50% damit
beschäftigt die eigentlichen Berechnungen durchzuführen, die zweite CPU war zu
ca. 50% damit beschäftigt den Finalizer und den Garbage-Collector auszuführen, um
die Objekte wieder zu verwerfen und deren Speicherplatz wieder zu verwerten. Das
Locking des GC bremste hier den Thread der die Berechnungen ausführte massiv
aus.

Als Konsequenz hiervon wurde die Klasse `ComplexNumber` so umgschrieben, dass die
Werte direkt veränderbar sind. Dadurch hat der Finalizer/GC nicht mehr viel zu
tun, denn der Thread der die Berechnung durchführt kann die CPU nun viel besser
auslasten. Je nach Zoom und Anzahl Schritte pro Pixel ist dies von 5%-50%
schneller. Da die zweite CPU nun nicht mehr mit dem Finalizer/GC beschäftigt
ist, würde eine Multi-Threaded-Implementation zusätzliche Performance
ermöglichen.

