# Documentazione Implementativa

Durante lo sviluppo dell'applicazione, mi sono occupato principalmente delle funzionalità relative ai Clienti, ai Conti Correnti e al Salvadanaio. Queste tre macro-aree sono state sviluppate in corrispondenza dei tre sprint. In alcune situazioni, è stata necessaria la collaborazione dei colleghi.

## Clienti

### Struttura generale

Inizialmente, in collaborazione con Bedei, sono stati sviluppati l'interfaccia e la classe implementativa di Person (vedi paragrafo Bedei... link).

l diagramma UML mostra che Customer estende Person, ereditando le proprietà e i comportamenti di quest'ultima. Customer aggiunge nuove funzionalità per la gestione degli appuntamenti, dei conti bancari e del concetto di fedeltà. Tuttavia, queste funzionalità comuni non sono implementate direttamente nelle classi concrete di Customer, ma in una classe astratta chiamata AbstractCustomer. Questo approccio rispetta il principio DRY (Don't Repeat Yourself), centralizzando l'implementazione comune e riducendo la duplicazione del codice.

In questo contesto, si potrebbe considerare l'utilizzo di un trait invece di una classe astratta per implementare i metodi comuni. La differenza tra i due approcci principalmente è che il trait supporta ereditarietà multipla mentre la classe astratta no. Un'altra particolarità delle classi astratte è che possiamo passare parametri e usare campi di classe.

Il concetto di `fidelity` è integrato nel design: ogni istanza di `Customer` ha associato un livello di fedeltà, gestito tramite un oggetto `Fidelity`. Questo permette di determinare dinamicamente, in base al livello di fedeltà del cliente, il calcolo delle tariffe base. Il pattern Strategy è applicato attraverso l'utilizzo dell'interfaccia `BaseFeeCalculator`, che definisce un metodo per calcolare la base fee. L'implementazione predefinita di questo calcolatore, `defaultBaseFeeCalculator`, utilizza un'algoritmo differente a seconda che il cliente sia giovane o meno e in base al suo livello di fedeltà.

Nello schema UML, la notazione (T) rappresenta un trait o un'interfaccia, mentre (C) indica una classe concreta che implementa quel trait o interfaccia. La presenza di (T, C) evidenzia l'utilizzo del pattern Factory per creare sia l'interfaccia che la sua classe concreta corrispondente, garantendo flessibilità e facilità di sostituzione delle implementazioni.

Questo approccio permette una separazione chiara tra l'interfaccia (o trait) che definisce il comportamento e la sua implementazione concreta, promuovendo così il principio di design SOLID di separazione delle responsabilità e facilitando l'estensibilità e il mantenimento del sistema nel tempo.

Di seguito una breve descrizione delle classi:

### Customer

Il Costumer rappresena il cliente.

Questo è una specializzazione della classe Person.

#### Meccanismi e pattern utilizzati

- **Pattern Factory** Il Pattern Factory è utilizzato con il metodo `apply` per creare istanze di `Customer`. A seconda dell'attributo `birthYear`, viene creata un'istanza di `YoungCustomerImpl` o `BaseCustomerImpl`. Questo approccio nasconde l'implementazione delle classi concrete all'esterno utilizzando il polimorfismo, viene restituito un implementazione del trait `BaseCustomer` o `YoungCustomer` che sono una specializzazione di `Customer`.
- **Pattern Decorator** Il Pattern Decorator è implementato tramite i mixin. Il trait `CustomerBehaviour` fornisce metodi per gestire gli appuntamenti, che vengono aggiunti al `YoungCustomer` e al `BaseCustomer` tramite mixin. Questo consente di estendere le funzionalità delle classi senza modificarne la definizione originale, promuovendo una maggiore modularità e riusabilità del codice.
Inoltre c'è un secondo utilizzo dei mixin, con il quale sono utilizzati i metodi/attributi di `Person`, attraverso l'export
- **Pattern Strategy** Il Pattern Strategy è realizzato utilizzando il meccanismo dei given. Il calcolo della `baseFee` è incapsulate in una implementazioni di `BaseFeeCalculator`. Questo permette di cambiare facilmente il comportamento del calcolo della `baseFee` semplicemente fornendo una diversa implementazione di `BaseFeeCalculator`, senza dover modificare le classi `Customer`.
- **Pattern Template Method** Il Pattern Template Method aiuto a creare una modello chiaro da seguire nelle implementazioni. Quindi sono state create prima le interfacce in cui sono stati definiti i comportamenti, e le associazioni, dopodichè sono stati creati le implementazioni sulla base delle interfacce.


