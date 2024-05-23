# Documentazione Implementativa

Nella prima parte mi sono occupato dello staff della banca in particolare, una volta creata la classe Persona in collaborazione con Mazzotti, sono passato a modellare le varie entità presenti nel contesto bancario.

## Parte 1

## Struttura generale

In particolare si è deciso di utilizzare il pattern factory per le se seguenti classi: `Person`, `Employee`, `Manager`, `Project`.
Riportiamo lo schema UML per la factory della persona.

![UML Persona](img/person.png)

Invece è stato utilizzato un template method per quanto concerne la gestione delle diverse tipologie di dipendenti. In particolare è stata creata prima l'interfaccia `StaffPosition` la quale è un mixin, il quale contiene implementati i metodi comuni ed estende Person. A questo punto le classi `Employee` e `Manager` estendono `StaffPosition`.
Riportiamo lo schema UML per il template method della gestione dei dipendenti. Si noti che (T) è un trait, (C) è una classe, (T, C) vuol dire che è stato creato il trait e la classe relativa utilizzando il pattern factory come descritto sopra per la classe `Persona`.

![UML Persona](img/template.png)

Ora riportiamo una sintetica descrizione delle classi:

### Person

Il trait `Person` rappresenta una persona con informazioni di base e relativi comportamenti.

#### Meccanismi utilizzati:

- **Validazione:** Controllo che l'anno di nascita non sia nel futuro con require.
- **Factory Method:** Metodo `apply` per creare istanze di `Person` con la case class `PersonImpl`.
- **Extension Methods** Per definire i metodi `isYoungerThan` e `ageDifference`.

### StaffMember

Il trait `StaffMember` rappresenta un membro dello staff con dettagli sulla posizione e metodi per calcolare i salari e gestire gli appuntamenti.
Essendo un mixin esso fornisce funzionalità comuni a tutti i membri dello staff. È un'implementazione generica che utilizza un parametro di tipo `T` che estende `StaffPosition`. 
Questa classe eredita da `Person`.

#### Meccanismi utilizzati:

- **Generics:** Utilizzo di generics al fine di poter generalizzare la posizione lavorativa di uno `StaffMember`, la quale si differenzia a seconda della tipologia di impiegato.
- **Bounded type parameter** Utilizzo di bounded type parameter `[T <: StaffPosition]`. Più specificamente un upper bound, in cui T è un parametro di tipo che può essere sostituito con qualsiasi tipo che sia un sottotipo di StaffPosition. In questo modo limitiamo i tipi di position.
- **Mixin** Il trait avendo sia metodi implementati che non è un mixin.
- **Currying** Per il passaggio di valori in `updateAppointment`.
- **List** Per la lista è stato deciso di usare una lista immutabile, usando una var.


### Employee

Il trait `Employee` rappresenta un dipendente che estende `AbstractStaffMember` con una specifica posizione lavorativa (`EmployeePosition`) e con possibilità di promozione con (`Promotable[EmployeePosition]`).

#### Meccanismi utilizzati:

- **Enumerazione:** `EmployeePosition` è un'enumerazione che definisce diverse posizioni e i relativi salari.
- **Givens:** L'uso di `given` per definire i tassi di bonus standard e senior e il tasso di imposta predefinito.
- **Factory Method:** Metodo `apply` per creare istanze di `Employee` con la case class `EmployeeImpl`.
- **For-yield** Utilizzo di for-yield per i metodi `allEmployeesSalary` e `totalAnnualSalary`.
- **Export** Utilizzo della delegazione su un oggetto `Person`.

### Manager

Il trait `Manager` rappresenta un manager e fornisce funzionalità aggiuntive per la gestione dei progetti.

#### Funzionalità Principali:

- **Gestione dei Progetti:** Metodi per ottenere (`projects`), aggiungere (`addProject`) e rimuovere (`removeProject`) progetti.
- **Calcoli sui Progetti:** Metodi estesi per ottenere il budget di un progetto specifico (`projectBudget`) e il budget totale di tutti i progetti (`totalProjectBudgets`).

#### Meccanismi utilizzati:

- **Enumerazione:** `ManagerPosition` è un'enumerazione che definisce diverse posizioni manageriali e i relativi salari.
- **Factory Method:** Metodo `apply` per creare istanze di `Manager` con la case class `ManagerImpl`.
- **Extension Methods** Per definire i metodi `projectBudget` e `totalProjectBudgets` su un manager e `totalProjectsManaged` su una lista di manager.
- **Tail_recursion** Per calcolare `totalProjectsManaged` per contre il numero di progetti fra tutti i manager.
- **Export** Utilizzo della delegazione su un oggetto `Person`.

### Project

Il trait `Project` rappresenta un progetto con un nome, un budget e un team di lavoro.


### StaffPosition

Il trait `StaffPosition` rappresenta una posizione occupata da un membro dello staff con il relativo salario.













Per tutte le seguenti implementazioni si è deciso di utilizzare un approccio TDD.
Il Test-Driven Development (TDD) è una metodologia di sviluppo software in cui i test automatizzati vengono scritti prima del codice funzionale. 
In particolare si è si è adottato il Red-Green-Refactor.
* Red: Scrivi un test che fallisce (red).
* Green: Scrivi il codice minimo per far passare il test (green).
* Refactor: Migliora il codice mantenendo tutti i test verdi.