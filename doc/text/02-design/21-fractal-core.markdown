### Kernstück ###

Dieses Abschnitt beschreibt das Kernstück der Fraktalberechnung, die Klasse
`ComplexEscapeFractal`.

* Berechnung im eigenen Thread um GUI nicht zu blockieren
* Verwendung des Scaler für Umrechnung von Bildschirmkoordinaten in skalierte
  Koordinaten des Fraktals und zurück.
* Schrittfunktion
* ColorMap für die Farbauswahl
* StatisticsObserver um GUI über Vorhandensein neuer Statistik-Daten zu
  informieren
* doDrawFractal: für jeden Bildpunkt werden die Anzahl Schritte gezählt bis max.
  Iterationen erreicht sind, oder die Begrenzung überschritten wird.
* doDrawOrbit: zeichnen des Orbit: nach jedem Schritt wird in
  Bildschirmkoordinaten zurückgerechnet, eine dünne Linie gezeichnet. Erste 30
  Schritte sind animiert, danch alles auf einmal für kürzere Wartezeit
* Zeichnen mittels der Java 2D API
