# DOCUMENTAZIONE IMPLEMENTATIVA NOTARO
Le mie principali responsabilità sono state:
- sviluppo del logger e gestione delle sue dipendenze
- modellazione dei mutui
- modellazione e gestione di trasferimenti tra conti correnti.

Nelle sezioni seguenti sono riportati e descritti gli aspetti implementativi ritenuti rilevanti per ogni compito da me svolto.

## Sviluppo del logger
Come anticipato nelle sezioni precedenti, lo sviluppo corretto di un logger può apparire un compito semplice e banale, tuttavia esso può risultare stimolante e più profondo di quanto appare se ci si pone come obiettivo quello di migliorare la gestione delle sue dipendenze e progettarlo seguendo quanto più possibile i principi di buona progettazione.

Il logger infatti, come mostrato anche più volte durante il corso, offre l'opportunità di misurarsi con aspetti non banali, relativi soprattutto all'applicazione di principi di buona progettazione del software e design pattern avanzati.

In particolare, il logger da me sviluppato mi ha dato l'opportunità di ragionare e lavorare sui seguenti aspetti:
- applicazione del cake pattern relativamente alla corretta gestione delle dipendenze delle classi client che utilizzano il logger
- applicazione del principio SRP relativamente alla gestione dei prefissi
- applicazione del principio DIP relativamente alla scelta del medium output.


Nella figura seguente è riportato uno schema riassuntivo che riporta gli aspetti implementativi rilevanti applicati al logger:
![schema riassuntivo principi logger](img/principiLogger.png)

Come suggerito dalla metodologia di sviluppo, anche lo sviluppo del logger ha seguito un processo incrementale e basato sul TDD.

La primissima versione del logger, infatti, era stata implementata come singleton, il che portava sicuramente a:
- vantaggi &rarr; semplicità di accesso da parte delle classi client ed inizializzazione centralizzata dei suoi attributi
- svantaggi &rarr; difficoltà nella gestione dinamiche delle sue configurazioni, rischio di eventuali dipendenza nascoste e scalabilità limitata.

### Applicazione del cake pattern
Visti tali svantaggi, è stato ritenuto opportuno passare ad una versione del logger che sfruttasse il pattern funzionale cake pattern.

Il cake pattern è un pattern funzionale in Scala che viene utilizzato per la corretta gestione delle dipendenze e la composizione dei moduli in modo più flessibile e modulare. 

Questo pattern sfrutta i mixin e i trait di Scala per risolvere in modo più elegante e flessibile il problema delle dipendenze evitando di dover utilizzare dipendenze esplicite o pattern Singleton.

I concetti chiave di tale pattern sono:
- traits per moduli &rarr; ogni modulo viene definito come trait
- self-type annotations &rarr; queste annotazioni permettono a un trait di dichiarare che deve essere mischiato (mixed-in) con un altro trait, esprimendo dunque una dipendenza
- composizione tramite mixins &rarr; i moduli vengono composti insieme utilizzando i mixin, creando istanze concrete che soddisfino tutte le dipendenze necessarie.

Di seguito viene mostrata l'implementazione del cake pattern al logger:
```
trait Logger:  // Trait defining the Logger functionality.
  ...
  def log(string: String): Unit

trait LoggerDependency:  // Trait for classes that depend on a Logger (cake pattern).
  val logger: Logger

class LoggerImpl extends Logger:  // Implementation of the Logger trait.
  ...
  def log(string: String): Unit =
    if isEnabled then outputMedia.println(prefixFormatter.getPrefixWithCurrentTime + string)
```
Vediamo poi come dev'essere implementata e strutturata una classe client per poter utilizzare le funzionalità offerte dal logger:
```
trait Employee

trait EmployeeComponent:
  loggerDependency: LoggerDependency =>  // Explicit dependency between EmployeeComponent and LoggerDependency.
  case class EmployeeImpl(...) extends Employee:
    ...
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    ...

// The object Employee extends both LoggerDependency and EmployeeComponent, providing also an implementation of the logger.
object Employee extends LoggerDependency with EmployeeComponent:
  override val logger: Logger = LoggerImpl()
  def apply(...): Employee = ...
```
Nel codice fornito si può notare come è stato applicato il cake patter tra Logger e Employee per gestire correttamente le dipendenze.

I componenti interessati dal pattern sono dunque:
- LoggerDependency &rarr; è il trait che rappresenta la dipendenze esposta, offerta dal logger
- EmployeeComponent &rarr; è il trait che definisce la struttura di Employee e contestualmente specifica che esso dipende da LoggerDependency
- ObjectEmployee combina dunque LoggerDependency ed EmployeeComponent, fornendo anche un'implementazione concreta del logger.

Come anticipato questa strategia, sebbene risulti più complessa da implementare e comprendere, offre notevoli vantaggi in termini di gestione delle dipendenze (esse sono chiaramente definite, migliorando di fatto anche la modularità) e separazione dei ruoli (la logica dell'impiegato risulta separata dalla logica del logger, pur dipendendone).

### Applicazione principio SRP per gestione prefissi
Il principio SRP suggerisce che un componente dovrebbe avere un unico motivo per cambiare.

Come conseguenza di ciò, occorre incapsulare via, all'esterno, ciò che ricade fuori dalla responsabilità principale del logger, ossia il PrefixFormatter, responsabile della gestione dei prefissi delle stampe.

Come conseguenza dell'applicazione di tale principio, è stato necessario separare le due classi, come di seguito riportato:
```
trait Logger:
  def log(string: String): Unit
  def getPrefixFormatter(): PrefixFormatter
  ...

class LoggerImpl extends Logger:
    private val prefixFormatter: PrefixFormatter = PrefixFormatter()
    ...

    def getPrefixFormatter(): PrefixFormatter = prefixFormatter
```
```
trait PrefixFormatter:
  def getPrefixWithCurrentTime: String
  def getCreationPrefix: String
  ...

object PrefixFormatter:
  def apply(): PrefixFormatter = PrefixFormatterImpl()

  private class PrefixFormatterImpl extends PrefixFormatter:
    private val timeFormatter = TimeFormatter()
    
    override def getPrefixWithCurrentTime: String = "[" + timeFormatter.getTimeFormatted() + "] "
    
    override def getCreationPrefix: String = "[CREATION] "
    ...
```

Alcune proprietà risultanti dal rispetto del principio di SRP sono: manutenibilità migliorata, semplificazione di testing e riusabilità.

### Applicazione del principio DIP per la scelta dell'output medium
L'ultimo aspetto rilevante di implementazione del logger che è bene sottolineare risulta essere l'applicazione del principio DIP per quanto concerne la scelta del mezzo output su cui effettuare le stampe del logger.

Infatti, nella sua accezione più semplificata, il logger stampa su console, tuttavia anche in ottica di sviluppi futuri (Anticipation Of Change) mi è sembrato ragionevole pensare che il logger potesse stampare anche su file ad esempio.

Introducendo tale vincolo di estendibilità, occorre cambiare la classe logger, in modo che segua il principio DIP, ovvero occorre imporre che la classe Logger non può dipendere da un'altra classe concreta (come System.out), ma bisogna sforzarsi di farlo dipendere da una sua astrazione.

Per rispettare tale principio, è stato necessario rimuovere la stampa diretta su console delle stringhe nel metodo log, ed estrarre ed aggiungere la dipendenza alla classe più generale PrintStream:
```
trait Logger:
    def log(string: String): Unit
    def setOutputMediaToFile(fileName: String): Unit
    def setOutputMediaToConsole(): Unit
    ...

class LoggerImpl extends Logger:
    private var outputMedia: PrintStream = System.out
    ...

    def log(string: String): Unit =
        if isEnabled then outputMedia.println(prefixFormatter.getPrefixWithCurrentTime + string)

    def setOutputMediaToFile(fileName: String): Unit = outputMedia = PrintStream(fileName + ".txt")

    def setOutputMediaToConsole(): Unit = outputMedia = System.out
```
Come conseguenza del rispetto di tale principio, si migliora la modularità e flessibilità del codice, riducendo inoltre le dipendenze rigide tra oggetti concreti.

## Modellazione dei mutui