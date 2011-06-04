## Tracking und Plannung ##

Für alle Features und Bugs wird im Redmine je ein Ticket erstellt. Dies
beschreibt das zu implementierende Feature oder den Bug. Dabei stellt das
Subject eine kurze Beschreibung dar, im Ticket selbst wird eine ausführlichere
Beschreibung eingegeben.


### Features ###

Ein Feature beschreibt gewünschtes Verhalten. Features werden mittels einfachen
Sätzen und Stichworten beschrieben. Bei der Implementation werden Features immer
in kleinen Schritten implementiert und getestet. Kleine Schritte erlauben eine
viel bessere Nachvollziehbarkeit und erleichtern das Review. Zu einem Feature
gibt es deshalb meistens mehrere Commits in der Git-Repository. Ein weiterer
Vorteil dieses Vorgehen ist es, das Verhalten der Software zu demonstrieren
bevor zu viel Zeit aufgewendet wurde. Ebenfalls hilft es dabei, das Design
bereits in einem frühen Stadium zu besprechen und gegebenenfalls zu ändern.


### Bugs ###

Wird ein Fehler in der Software entdeckt, wird dies im Redmine festgehalten. Ist
der Fehler in einem Feature das noch nicht vollständig implementiert ist, wird
ein Kommentar im Feature-Ticket hinzugefügt, der den Fehler beschreibt. Betrifft
der Fehler etwas anderes, wird im Redmine ein Bug-Ticket erstellt. Dieses
beschreibt ebenfalls das Verhalten mit einer Anleitung, wie Schritt für Schritt
der Fehler reproduziert werden kann.


### Planung ###

Da die Entwicklung iterativ erfolgt, werden mehrere kleinere Pre-Releases
erstellt, die jeweils demonstriert und besprochen werden können. Um diese
Releases zu planen, werden die Feature- und Bug-Tickets priorisiert. Anhand der
Priorisierung wird entschieden in welchen Pre-Release (Meilenstein) die
Änderungen einfliessen. Ein Release wird auf ein bestimmtes Datum geplant. Je
nachdem, wie gut die Entwicklung voran geht, können Features von einem späteren
Release bereits früher implementiert werden, oder Features in einen späteren
Release verschoben werden. Bugs werden immer so schnell wie möglich behoben.
