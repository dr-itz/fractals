## Mandelbrot Fraktale ##

Das Mandelbrot-Fraktal ist ein Fraktal in der komplexen Zahlenebene. Auf die
genaue Definition wird hier nicht weiter eingegangen. Um die Mandelbrot-Menge zu
visualisieren eignet sich ein sogenannter "Escape Time Algorithm", also ein
Algorithmus der für jeden Bildpunkt die Anzahl Iterationen zählt, mit der eine
iterative Folge aus einer Beschränkung "flüchten" kann. Dabei wird auch eine
maximale obere Grenze definiert, ab der abgebrochen wird.

* Die Iterationsvorschrift ist dabei:

	$z_{n+1} = z_n^2 + z_0$


* Die Beschränkung:

	$|z| < 2$

* Die Farbe eines Bildpunktes wird aus der Anzahl Iterationen abgeleitet


## Julia-Fraktale ##

Die Julia-Fraktale sind sehr ähnlich zu dem Mandelbrot-Fraktal.

* Die Iterationsvorschrift ist dabei:

	$z_{n+1} = z_n^2 + c$

D.h. es wird nicht die Anfangszahl $z_0$ addiert, sondern eine frei wählbare
Konstante $c$.
