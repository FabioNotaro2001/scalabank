# DOCUMENTAZIONE IMPLEMENTATIVA BERTUCCIOLI

I compiti a me assegnati durante lo sviluppo riguardavano:

- realizzazione dell'interfaccia grafica tramite monadi
- modellazione e realizzazione di filiali e appuntamenti
- ultimi ritocchi all'interfaccia grafica per completare l'applicazione

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
```scala
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

```scala
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

## Parte 2: modellazione e realizzazione di filiali e appuntamenti

### Interfacce

Le interfacce principali che ho realizzato per implementare banche e appuntamenti sono `Bank` e `Appointment`:

- `Bank` include metodi per l'aggiunta di clienti e dipendenti e metodi per creare, modificare ed eliminare appuntamenti.
Inoltre, contiene un addizionale metodo per reperire informazioni sulla banca. L'interfaccia non include metodi per la modifica
di tali informazioni, ma se necessario queste possono modificate creando un'implementazione mutabile dell'intefaccia `BankInformation`.

```scala
trait BankInformation

trait Bank:
  def bankInformation: BankInformation
  def addEmployee(employee: Employee): Unit
  def addCustomer(customer: Customer): Unit
  def createAppointment(customer: Customer, description: String, date: LocalDateTime, duration: Int): Appointment
  def updateAppointment(appointment: Appointment, description: Option[String], date: Option[LocalDateTime], duration: Option[Int]): Appointment
  def cancelAppointment(appointment: Appointment): Unit
```

- `Appointment` prevede metodi per l'accesso ai dati dell'appuntamento, nello specifico cliente, dipendente, descrizione,
data e durata prevista dell'appuntamento.

```scala
trait Appointment:
  def customer: Customer
  def employee: Employee
  def description: String
  def date: LocalDateTime
  def duration: Int
```

### Implementazioni

Per l'implementazione delle due interfacce principali ho utilizzato il pattern `Cake` per l'aggiunta della dipendenza dal logger e
il pattern `Static Factory` per la creazione di istanze delle classi realizzate. 

L'applicazione prevede due tipi diversi di filiali: quelle fisiche e quelle virtuali. Per tale ragione, esistono due classi che
implementano `Bank`, le quali sono `PhysicalBank` e `VirtualBank`. 
Durante la realizzazione, inoltre, ho deciso di realizzare la classe astratta generica `AbstractBank`, 
che contiene campi e metodi comuni a entrambe le implementazioni. 
Il tipo generico di tale classe si riferisce al tipo delle informazioni che le implementazioni utilizzano.

Siccome le banche virtuali non includono la possibilità di creare appuntamenti, i metodi in questione inclusi nell'interfaccia
si limitano a generare un'eccezione del tipo `UnsupportedOperationException`.

Di seguito si può vedere come sono realizzate le filiali:

```scala
abstract class AbstractBankImpl[T <: BankInformation](override val bankInformation: T) extends Bank:
  protected val employees: ListBuffer[Employee] = ListBuffer()
  protected val customers: ListBuffer[Customer] = ListBuffer()

  override def addEmployee(employee: Employee): Unit =
    employees.addOne(employee)
  override def addCustomer(customer: Customer): Unit =
    customers.addOne(customer)
```
```scala
trait BankComponent:
  loggerDependency: LoggerDependency =>
    
  case class PhysicalBankInformation(name: String, address: String, phoneNumber: String) extends BankInformation

  case class PhysicalBank(bankInfo: PhysicalBankInformation) extends AbstractBankImpl[PhysicalBankInformation](bankInfo):
    private val appointments: MutableMap[Customer, ListBuffer[Appointment]] = MutableHashMap()
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)

    override def createAppointment(customer: Customer, description: String, date: LocalDateTime, duration: Int): Appointment =
      // ...
    override def updateAppointment(appointment: Appointment, description: Option[String], date: Option[LocalDateTime], duration: Option[Int]): Appointment =
      // ...
    override def cancelAppointment(appointment: Appointment): Unit =
      // ...

  case class VirtualBankInformation(name: String, phoneNumber: String) extends BankInformation
  
  case class VirtualBank(bankInfo: VirtualBankInformation) extends AbstractBankImpl[VirtualBankInformation](bankInfo):
    loggerDependency.logger.log(logger.getPrefixFormatter().getCreationPrefix + this)
    
    override def createAppointment(customer: Customer, description: String, date: LocalDateTime, duration: Int): Appointment =
      throw UnsupportedOperationException("Virtual banks don't support appointments")
    override def updateAppointment(appointment: Appointment, description: Option[String], date: Option[LocalDateTime], duration: Option[Int]): Appointment =
      throw UnsupportedOperationException("Virtual banks don't support appointments")
    override def cancelAppointment(appointment: Appointment): Unit =
      throw UnsupportedOperationException("Virtual banks don't support appointments")
```

Il companion object di `Bank` funge da Static Factory per la creazione di istanze delle filiali e definisce il logger che
le classi utilizzano al loro interno, come da pattern Cake.

```scala
object Bank extends LoggerDependency with BankComponent:
  override val logger: Logger = LoggerImpl()

  def physicalBank(name: String, address: String, phoneNumber: String): Bank = PhysicalBank(PhysicalBankInformation(name, address, phoneNumber))

  def virtualBank(name: String, phoneNumber: String): Bank = VirtualBank(VirtualBankInformation(name, phoneNumber))
```

### Test

Per assicurarmi del corretto funzionamento delle implementazioni ho realizzato dei test seguendo l'approccio TDD, nel quale 
si realizzano prima dei test che non passano e poi si modifica il codice per farsi che i test abbiano successo. In questo modo
ho testato la creazione, rimozione e modifica degli appuntamenti.

## Parte 3: ultimi ritocchi all'interfaccia grafica per completare l'applicazione

In questo sprint, ho combinato tutte le funzionalità create dagli altri partecipanti, inserendole nell'interfaccia grafica
per renderla funzionante, implementando i comportamenti dovuti alla pressione di bottoni dell'interfaccia.

### Implementazione

La gestione degli eventi lanciati dall'interfaccia è fatta mediante monadi, come si può vedere dal seguente esempio:

```scala
case "User-Home-Appointments" =>
    for
      _ <- updateList("Appt-List", customer.get.getAppointments.map(_.toStringFromCustomerSide).toArray)
      _ <- showView("User-Appointments")
    yield ()
```
