### Der Scaler ###

Die Klasse `Scaler` ist für das Skalieren von Bildschirmkoordinaten zu den
Koordinaten der Fraktalfunktion zuständig. Die folgenden Funktionen sind
implementiert:

* Umrechnung von Bildschirmkoordinaten zu Fraktalkoordinaten
* Umrechnung von Fraktalkoordinaten zu Bildschirmkoordinaten 
* Zoom in einen bestimmten Bereich
* Zentrieren des Bildes

Der Scaler verzichtet dabei bewusst auf die Verwendung von komplexen Zahlen um
Rechenzeit zu sparen. Der Grund hierfür liegt in der Iteration der einzelnen
Pixel: Die äussere Schleife iteriert über die Zeilen, die innere Schleife
iteriert über die einzelnen Pixel. Die Zeilen stellen den Imaginär-Teil der
komplexen Zahlen dar. Dieser muss nur einmal pro Zeile berechnet werden. Würden
direkt komplexe Zahlen verwendet werden, würde bei jedem Pixel der Imaginär-Teil
neu berechnet werden, was unnötig Zeit braucht.

Merkpunkte zum Scaler:

* Der Scaler wird anhand der Fraktal-Schrittfunktion initialisiert
* Das GUI setzt die Grösse des angezeigten Bildes
* Bei der Selektion eines Bereiches teilt das GUI dies dem Scaler mit, welcher
  den Zoom damit neu berechnet.

