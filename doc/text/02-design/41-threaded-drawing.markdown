## Erweiterung: Zeichnen in Threads ##

Eine mögliche Erweiterung ist das Zeichnen der Fraktale in mehreren Threads.
Hier wurde ein Kompromiss zwischen Einfachheit und Geschwindigkeit gemacht.
Anstelle einer konfigurierbaren Anzahl von Threads sind genau zwei Threads mit
dem Zeichnen des Fraktals beschäftigt. Beim Zeichnen von grob zu fein wurde ein
zweiter Thread eingeführt. Der letzte Schritt braucht am meisten Zeit, da hier
75% aller Pixel berechnet werden. Um die Berechnung zu beschleunigen, kümmert
sich ein separater Thread um das Errechnen dieses letzten Schrittes, der
gestartet wird bevor von grob nach fein aufgelöst wird. Dieser läuft dann also
parallel zur Auflösung von grob nach fein.

Diese Methode des Multithreadings bringt zwar keine optimale CPU-Auslastung,
ist aber sehr einfach implementierbar, ohne dass die Lesbarkeit und die
Wartbarkeit des Codes darunter leidet. Der Geschwindigkeitsgewinn ist immerhin
ca. 25%.
