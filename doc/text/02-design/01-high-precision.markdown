## Hohe Präzision beim Rechnen ##

Eine der möglichen Erweiterungen ist das Benutzen von Arbitrary Precision Math,
also das Rechnen mit beliebiger Präzision um die Grenzen des normalen "double"
zu erweitern. Da diese Art des Rechnen aufwändiger ist, wurden vorgänig
Messungen durchgeführt, um entscheiden zu können, ob diese Erweiterung beim
Design der Software überhaupt berücksichtigt werden soll.

Um diese Messungen durchführen zu können, wurden minimale Implementationen des
Mandelbrot-Fraktals vorgenommen, ohne Zoom oder andere Features. Eine
Java-Library die Arbitrary Precision Math implementiert ist "Apfloat", diese
gilt als sehr performant. Ein anderer Ansatz, höhere Präzision zu erreichen ist
das Rechnen mit zwei statt nur einem "double" pro Zahl. Die Algprithmen hierzu
sind von Knuth, Kahan, Dekker, und Linnainmaa beschrieben und von Martin Davis
in der Java-Klasse "DoubleDouble" implementiert.


### Implementationen ###

#### Normaler "double" ####

TODO


#### DoubleDouble ####

TODO


#### Apfloat ####

TODO


### Vergleich der Performance ###

TODO
