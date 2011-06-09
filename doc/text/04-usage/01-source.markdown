## Der Source-Code und Build-Prozess ##

### Verzeichnisstruktur ###

Die Sourcen sind nach der folgenden Struktur organisiert:

 * **build/**: Vom Build-Prozess generierte Dateien wie Class-File.
 * **dist/**: Hier befindet sich nach erfolgreichem Build das JAR-File.
 * **doc/**: Die Quellen der Dokumentation.
 * **lib/**: Alle für den Build-Prozess erforderlichen Libraries
 * **src/main/**: Die Sourcen der Applikation.
 * **src/test/**: Die Sourcen der Unit-Tests.


### Builden der Applikation ###

Mit den Sourcen wird ein Eclispe-Projekt sowie ein Ant-Build mitgeliefert.

* Kompilieren der Applikation und erstellen des JARs:

~~~~ 
ant app
~~~~ 


* Automatische Tests ausführen:

~~~~ 
ant test
~~~~ 

* Automatische Tests mit zusätzlichen Coverage-Report ausführen. Der Report kann
  unter `build/cobertura/html/index.html` angeschaut werden:

~~~~ 
ant coverage
~~~~ 


Alle weiteren Targets des Build-Prozesses sind beschrieben, wenn `ant` ohne
weitere Parameter ausgeführt wird.


### Builden der Dokumentation ###

Die Dokumentation ist in Pandoc-Markdown geschrieben und wird mittels Pandoc und
\LaTeX in ein PDF umgewandelt. Das Script dafür ist in Ruby geschrieben.
Folgende Tools müssen installiert sein:

* Ruby
* Pandoc 1.8
* \XeTeX. Enthalten in MacTex für MacOS X oder MikTex für Windows

Um das PDF zu erstellen:

~~~~ 
./doc/latex/makepdf
~~~~ 

