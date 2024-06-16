# Documentazione Implementativa

I compiti a me assegnati durante lo sviluppo riguardavano:

- realizzazione dell'interfaccia grafica tramite monadi
- modellazione e realizzazione delle filiali
- modellazione e realizzazione degli appuntamenti

## Parte 1: realizzazione dell'interfaccia grafica

### Interfacce

Come scritto sopra, l'interfaccia grafica è realizzata mediante l'uso di monadi.
Come base per la sua realizzazione ho utilizzato un precedente esempio di creazione di GUI visto a lezione,
propriamente modificato per aggiungere le funzionalità necessarie per realizzare l'interfaccia, tra cui pannelli e componenti specifici.

A tale scopo, sono state realizzate due interfacce:

- `Frame`, scritta in Java
```java
public interface Frame {
    String CLOSED = "CLOSED";
    Frame setSize(int width, int height);
    Frame addView(String name, LayoutManager layout);
    Frame showView(String name);
    Frame addPanel(String name, LayoutManager layout, String panel, Object constraints);
    // ...
}
```
- `WindowState`, scritta in Scala, che rispecchia `Frame` e fa uso di monadi
```scala 3
trait WindowState:
  type Window
  def initialWindow: Window

  def setSize(width: Int, height: Int): State[Window, Unit]
  def addView(name: String, layout: LayoutManager): State[Window, Unit]
  def showView(name: String): State[Window, Unit]
  def addPanel(name: String, layout: LayoutManager, panel: String, constraints: Any): State[Window, Unit]
  // ...
```

### Implementazioni

L'interfaccia `Frame` è implementata da `FrameImpl`, la quale funge da facciata per lavorare con Java Swing,
mentre il trait `WindowState` è implementato da `WindowStateImpl`, che si appoggia su `FrameImpl`.

L'uso di monadi (nello specifico mediante la classe `State`) consente, in Scala,
di realizzare delle pipeline di chiamate a metodi usando il costrutto `for-yield` per snellire il codice, come si può vedere nel seguente esempio:

```scala 3
val emplAppointmentsView = for
_ <- addView("Empl-Appointments", BorderLayout(0, 10))
_ <- addButton("Empl-Appts-Back", "<", "Empl-Appointments", BorderLayout.WEST)
_ <- addPanel("Empl-Appts-Panel-Outer", FlowLayout(), "Empl-Appointments", BorderLayout.CENTER)
_ <- addPanel("Empl-Appts-Panel", GridLayout(2, 1), "Empl-Appts-Panel-Outer", FlowLayout.CENTER)
_ <- addLabel("Empl-Appts-List-Label", "Lista appuntamenti", "Empl-Appts-Panel", FlowLayout.CENTER)
_ <- addList("Empl-Appts-List", Array(), "Empl-Appts-Panel", null)
yield ()
```

Il codice sopra indicato realizza una vista dell'interfaccia grafica, composta da un bottone, un'etichetta e
una lista di elementi posizionati dentro a dei pannelli.
Le altre viste sono scritte in modo analogo per realizzare l'interfaccia grafica completa.

### Test

Per verificare il corretto comportamento del codice, sono stati realizzati alcuni test che si
occupano principalmente di verificare che i principali casi di errore siano gestiti correttamente, come ad esempio il fatto
che non si possono creare più componenti dello stesso tipo con lo stesso nome.