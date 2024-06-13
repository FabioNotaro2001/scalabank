# Documentazione Implementativa

Durante lo sviluppo dell'applicazione, mi sono occupato principalmente delle funzionalità relative ai Clienti, ai Conti Correnti e al Salvadanaio. Queste tre macro-aree sono state sviluppate in corrispondenza dei tre sprint, principalmente da me. Tuttavia, in alcune situazioni, è stata necessaria la collaborazione dei colleghi.

## Parte 1

### Struttura generale

Inizialmente, in collaborazione con Bedei, è stato sviluppata l'interfaccia e la classe implementativa di Person (vedi paragrafo Bedei... link).

Come si può notare dal seguente diagramma UML, l'interfaccia `Customer`, che estende `Person`, viene ulteriormente specializzata nelle classi `BaseCustomer` e `YoungCustomer`. Queste due classi rappresentano implementazioni concrete di `Customer`, utilizzando il pattern Factory per la loro creazione e gestione.

Il concetto di `fidelity` è integrato nel design: ogni istanza di `Customer` ha associato un livello di fedeltà, gestito tramite un oggetto `Fidelity`. Questo permette di determinare dinamicamente, in base al livello di fedeltà del cliente, il calcolo delle tariffe base. Il pattern Strategy è applicato attraverso l'utilizzo dell'interfaccia `BaseFeeCalculator`, che definisce un metodo per calcolare la base fee. L'implementazione predefinita di questo calcolatore, `defaultBaseFeeCalculator`, utilizza un'algoritmo differente a seconda che il cliente sia giovane o meno e in base al suo livello di fedeltà.

Nello schema UML, la notazione (T) rappresenta un trait o un'interfaccia, mentre (C) indica una classe concreta che implementa quel trait o interfaccia. La presenza di (T, C) evidenzia l'utilizzo del pattern Factory per creare sia l'interfaccia che la sua classe concreta corrispondente, garantendo flessibilità e facilità di sostituzione delle implementazioni.

Questo approccio permette una separazione chiara tra l'interfaccia (o il trait) che definisce il comportamento e la sua implementazione concreta, promuovendo così il principio di design SOLID di separazione delle responsabilità e facilitando l'estensibilità e il mantenimento del sistema nel tempo.

