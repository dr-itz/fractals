## Projektbeschreibung ##

In einem GUI können Mandelbrot- und Julia-Mengen dargestellt werden. Dabei
werden die folgenden generellen Features implementiert:

  * **Auswahl Mandelbrot oder Julia**

	Eine einfache Auswahl lässt den Benutzer zwischen der Visualisierung der
	Mandelbrot- und der Julia-Menge wählen.

  * **Visualisierung**

	Der Hauptteil der Ansicht ist eine Fläche in der die Fraktale graphisch
	dargestellt werden. Verschiedene Arten der Darstellung stehen zur Auswahl.

  * **Zoom-Funktion**

	Eine Zoom-Funktion durch Selektion mit der Maus erlaubt, Fraktale bis zu
	einer gewissen Grenze zu vergrössern.

  * **Reset**

	Ein Reset-Button löscht die gesamte Visualisierung.



Spezifisch für Mandelbrot und Julia sind die folgenden Features:

  * **Maximale Anzahl Berechnungen**

	Es kann vorgegeben werden, wie viele Berechnungen pro Punkt maximal
	durchgeführt werden. Die Vorgabe ist 100.

  * **Farbauswahl**

	Es kann zwischen einer Darstellung in Schwarz-weiss und Farbe gewählt
	werden.

  * **Konstante für die Julia-Menge**

	Für die Julia-Menge kann die Konstante vorgegeben werden.

  * **Zeichnen der Menge**

	Ein Button zeichnet das Gesamte Fraktal im sichtbaren Bereich.

  * **Zeichnen einzelner Linien**

	Eine weitere Funktion erlaubt das Zeichnen einzelner Wege mit allen
	Gliedern bis zu der konfigurierten Anzahl Berechnungen. Dabei kann die Farbe
	ausgewählt werden. Durch anklicken eines Punktes in der graphischen
	Darstellung wird der Vorgang gestartet. Um die Visualisierung zu verbessern,
	wird dabei folgendes implementiert:

	  * **Verzögerung zwischen den Schritten**

		Eine Auswahl erlaubt die Angabe in Millisekunden die zwischen den
		einzelnen Schritten gewartet werden soll. Dadurch kann der Weg besser
		verfolgt werden.

	  * **Automatische Farbauswahl**

		Als Option einschaltbar ist die automatische Selektion einer neuen Farbe
		bei jedem zusätzlichen Weg. Dadurch sind diese einfach
		auseinanderzuhalten.



Für das Design und die Architektur der Software wird auf folgendes geachtet:

* Genügend Abstraktion um ggf. Berechnungen mit "Arbitrary Precision Arithmetic"
  durchzuführen.
* Trennung von GUI und Berechnungen
* Einfach austauschbare Algorithmen
* Alle generisch verwendbaren Berechnungsfunktionen sind getrennt von den
  eigentlichen Fraktal-Algorithmen
* Erweiterbarkeit


### Mögliche Erweiterungen ###

Folgendes sind mögliche Erweiterungen, die je nach verfügbarer Zeit
implementiert werden können und beim Design der Software von Anfang an
berücksichtigt werden:

  * **Finden von Zyklen**

	Es gibt Punkte, bei denen der Weg wieder zum Ausgangspunkt zurück führt.
	Diese Zyklen sollen durch das numerische Lösen der dazugehörigen Gleichungen
	gefunden werden. Mindestens Zyklen mit drei Punkten sollen gefunden werden.

  * **Arbitrary Precision Arithmetic**

	Um eine detailliertere Darstellung mit einem viel stärkerem Zoom zu
	ermöglichen, soll Arbitrary Precision Arithmetic zum Einsatz kommen.

  * **Multithreading**

	Die meisten modernen Prozessoren haben mehrere Kerne. Daher macht es sinn,
	die Software so aufzubauen, dass mehrere Threads parallel die Visualisierung
	der Fraktale berechnen um so die Geschwindigkeit zu erhöhen.

  * **Magnetpendel**

	Ein Magnetpendel <http://de.wikipedia.org/wiki/Magnetisches_Pendel> ist ein
	chaotisches System und somit eine konkrete physikalische Anwendung von
	Fraktalen. Dabei geht es um das numerische lösen von
	Differentialgleichungen. Der Grundaufbau mit der Visualisierung ist bereits
	sehr ähnlich.
