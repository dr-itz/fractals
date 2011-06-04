# Qualitätssicherung #

Zu jeder Softwareentwicklung gehört auch Qualitätssicherung dazu. Die
Qualitätssicherung in diesem Projekt setzt sich aus den folgenden Teilen
zusammen:

  * **Tracking**

	Alle Features und Bugs werden in Redmine mittels entsprechenden Feature-
	oder Bug-Tickets verfolgt. Dies stellt sicher, dass nichts vergessen geht
	und erlaubt die einfache Release-Planung.

  * **Code-Review**

	Bevor Code der Git-Repository hinzugefügt wird, wird jeweils noch ein Review
	durchgeführt. Dazu eignet sich die Diff-Ansicht des Git-GUI. Nachdem Code in
	der Git-Repository geändert wurde, wird dieser jeweils nochmals von anderen
	Entwickler(innen) angeschaut und kurz getestet.

  * **Testen**

	Alle Features werden getestet. Wo dies einfach möglich ist, kommen
	automatische Unit-Tests mittels JUnit 4 zum Einsatz. Wo dies nicht einfach
	möglich ist, werden Tests von Hand durchgeführt.

  * **Statische Code-Analyse**

	Ein weiteres Instrument um Probleme im Code zu finden, ist statische
	Code-Analyse. Hier kommt als Tool das bekannte FindBugs der University of
	Maryland zum Einsatz. Dieses analysiert den kompilierten Java-Code ohne ihn
	auszuführen und weisst auf mögliche Probleme hin.
