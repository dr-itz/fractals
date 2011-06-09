# Benutzeranleitung #

## Bedienung der Applikation ##

![Screenshot der Applikation mit Mandelbrot und einem Orbit](figures/screenshot.png)


### Starten der Applikation ###

Voraussetzung für die Lauffähigkeit der Applikation ist ein installiertes Java
6. Auf MacOS X ist dies bereits mitgeliefert. Die Applikation wird als
ausführbares JAR geliefert. Auf den meisten Plattformen kann dies durch einen
einfachen Doppelklick gestartet werden. Alternativ ist ein Starten von der
Kommandozeile mit dem folgenden Befehl möglich:

~~~~ 
java -jar fractals-1.0.jar
~~~~ 


Direkt nach dem Start wird ein Mandelbrot-Fraktal in Graustufen gezeichnet. Die
Applikation selbst ist so gut wie möglich selbsterklärend und sollte intuitiv
bedienbar sein. In den folgenden Abschnitten werden die einzelnen Features kurz
beschrieben.


### Aufbau der Applikation ###

Die Benutzeroberfläche besteht bewusst nur aus einem einzigen Fenster. Dieses
ist in die folgenden Bereiche eingeteilt:

  * **Anzeige**

	Der grösste Teil des Fenster ist von der Anzeige des Fraktals benutzt.

  * **Frakalauswahl und Konfiguration**

	Unten links befindet sich der Bereich für die Auswahl und Konfiguration der
	Darstellung des Fraktals. Hier kann die Funktion, die maximale Anzahl
	Iteration pro Bildpunkt, das Frabschema, sowie für Julia-Fraktale die
	Konstante eingegeben werden.

  * **Informationsanzeige**

	Oben rechts befindet sich die Informationsanzeige ("Info"). Hier werden die folgenden
	Informationen dargestellt:

	  * Die aktuelle Koordinaten unter dem Mauszeiger als komplexe Zahl.
	  * Der aktuelle Zoom, als ganze Zahl.
	  * Die Zeit die benötigt wurde, um die aktuelle Anzeige zu berechnen.
	  * Die Anzahl Schritte die berechnet wurden. An dieser Stelle erscheint
	    während einer laufenden Berechnung eine Fortschrittsanzeige.
	  * Die Anzahl gefundenen Zyklen für den Zyklus-Finder. Während der
	    Berechnung erscheint hier auch eine Fortschrittsanzeige.

  * **Verhalten der Maus**

	Direkt unterhalb der Informationsanzeige lässt sich das Verhalten der Maus
	konfigurieren ("Click Action"). Hier kann ausgewählt werden, ob mit der
	Maus der Zoom kontrolliert wird, oder ob Orbits gezeichnet werden sollen.

  * **Zeichnen der Orbits**

	Unten rechts befinden sich alle Elemente bezüglich Zeichnen der Orbits
	("Orbit drawing"). Hier lässt sich die Farbe konfigurieren, von Hand ein
	Startwert für einen Orbit angeben, oder automatisch zyklische Orbits finden.


### Zeichnen von Mandelbrot und Julia Fraktalen ###

Unten links kann die Fraktalfunktion ausgewählt werden. Beim Ändern der Auwahl
wird sofort das Fraktal gezeichnet. Rechts davon lässt sich die maximale
Iterationszahl konfigurieren. Wird diese verändert, muss das Fraktal durch
klicken auf "Draw" neu gezeichnet werden. Beim der Auswahl eines Julia-Fraktals
werden direkt unterhalb der Auswahl zwei Zusäzliche Eingabefelder eingeblendet.
Diese erlauben das festlegen der Konstante. Die beiden Eingabefelder stellen den
Real- und den Imaginärteil dieser Konstante dar. Wird die Konstante geändert,
muss ebenfalls durch den "Draw"-Button das Frakal neu gezeichnet werden.

Um die farbliche Darstellung des Fraktals zu ändern, stehen zwei Komboboxen zur
unter "Colorization" zur Auswahl. Die linke legt dabei das Farbschema für die
Punkte fest, die sich ausserhalb der Mandelbrot- bzw. Julia-Menge befinden. Die
farblichen Abstufungen helfen dabei zu zeigen, wieviele Schritte notwendig sind
um einen Betrag > 2 zu erreichen. Die zweite Auswahl, "Set color" legt die Farbe
der Menge selbst fest, als jener Punkte dessen Orbit gebunden sind. Änderungen
an der Frabauswahl werden sofort übernommen


### Zoom ###

Ist in der Auswahl "Click action" der Radio-Button "Zoom" angewählt, kann mit
der Maus der Zoom verändert werden. Um einen Bereich heranzuzoomen, kann dieser
einfach ausgewählt werden mittels Klicken-und-Ziehen. Die ausgewählte Fläche
wird hierbei hervorgehoben. Das Fraktal wird danach für den gewählten Ausschnitt
neu berechnet und die Informationsanzeige entsprechend aktualisiert. Um den Zoom
wieder zu verringern, kann einfach die rechte Maustaste betätigt werden. Der
Punkt unter den Mauszeiger wird dabei das neue Zentrum der Darstellung, der Zoom
wird um Faktor 3 verringert.


### Zeichnen von Orbits ###

Ist in der Auswahl "Click action" der Radio-Button "Orbit drawing" angewählt,
können mit der Maus die Orbits eingezeichnet werden. Wird ein Punkt im Fraktal
angeklickt, wird dieser als Startwert genommen und der Orbit wird in der
entsprechenden Farbe, wählbar unter "Orbit drawing" gezeichnet. Die Checkbox
"Auto-cycle" kontrolliert hierbei die automatische Umschaltung auf die nächste
Farbe in der Liste. Die Zahl "Step delay" legt fest, wieviele Millisekunden
zwischen dem Zeichnen der ersten 30 Schritten eines Orbits gewartet werden soll.
Der Button "Clear orbits" löscht alle bisher eingezeichneten Orbits. Der
Startwert beim Zeichnen eines Orbits wird dabei im Bereich "Manual Real/Imag"
eingetragen. Dies erlaubt dem Benutzer, die Werte zu verändern und durch den
Button "Draw orbit" einzuzeichnen.

Wird mit gedrückter Taste die Maus bewegt, wird mit weisser Farbe der Orbit an
dieser Stelle live gezeichnet und wird erst beim loslassen der Maus bleibend und
in der ausgewählten Farbe eingezeichnet. Mit der rechten Maustaste bleiben die
Live-Orbits auch nach den Loslassen der Maustaste bestehen, können aber durch
erneutes klicken wieder entfernt werden.


### Finden von Zyklen ###

In der untersten Zeile im Bereich "Orbit drawing" können zyklische Orbits
mittels einer Newton-Iteration gefunden werden. Zuerst wird die Länge des Zyklus
mit "Find cycles of length .." festgelegt. Der "Delay" kontrolliert die
Verzögerung in Millisekunden bei zwischen dem Anzeigen der einzelnen Orbits. Die
Checkbox "Full orbit" legt fest, ob für jeden gefundenen Startpunkt der gesamte
Orbit gezeichnet werden sollt. Ist diese Checkbox nicht angewählt, werden nur
die Startpunkte eingezeichnet. Durch den Button "Find" kann die Suche gestartet
werden. Alle einzeichneten Orbits benutzen dabei die Farbeinstellungen und
lassen sich durch "Clear orbits" wieder löschen.


### Speichern der Ansicht ###

Die aktuelle Ansicht des Fraktal lässt sich als Bild abspeichern. Dazu muss der
Button "Save as..." betätigt werden. Nach Eingabe des Dateinamen und Bildformat
(PNG oder JPEG) wird die Ansicht abgespeichert.

