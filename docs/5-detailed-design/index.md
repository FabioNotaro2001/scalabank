# Design di dettaglio
<!--Design di dettaglio (scelte rilevanti, pattern di progettazione, organizzazione del codice -- corredato da pochi ma efficaci diagrammi)
l design di dettaglio "esplode" (dettaglia) l'architettura, ma viene concettualmente prima dell'implementazione, quindi non metteteci diagrammi ultra-dettagliati estratti dal codice, quelli vanno nella parte di implementazione eventualmente.-->
Nel design di dettaglio, per far emergere le scelte rilevanti e l'organizzazione del codice, si è ritenuto ragionevole produrre alcuni diagrammi delle classi UML a partire dal diagramma dei package prodotto nella sezione precedente di design architetturale.

Nelle sezioni che seguono vengono dunque riportati e descritti i diagrammi prodotti.

## Design di dettaglio delle persone coinvolte nell'ambito bancario
Come emerso anche nel diagramma dei package prodotto nella fase di design architetturale, le varie persone coinvolte nel contesto bancario possono essere rifinite e descritte con maggiore precisione grazie al seguente diagramma delle classi UML:
![uml entities](img/UMLEntità.png)
I componenti che compongono il package entities sono dunque:
- il trait Person contenente dei campi pubblici per tutti i suoi dati anagrafici rilevanti &rarr; tale trait rappresenta la superclasse di una gerarchia che comprende i sottotrait StaffMember e Customer
- il trait Customer rappresenta la specializzazione di Person atta a rappresentare i clienti
- invece il trait StaffMember rappresenta la specializzazione di Person che rappresenta gli impiegati bancari &rarr; si noti tra le altre cose che tale trait ha dei metodi e campi per lavorare con gli appuntamenti e che è la superclasse di un'altra gerarchia che comprende i sottotrait Manager ed Employee
- esiste poi anche il trait Promotable, per esprimere che è possibile che un Employee venga promosso ad una nuova posizione
- esiste infine anche il trait+implementazione Project che esprime in quali progetti è impegnato ciascun membro dello staff &rarr; si noti che ogni progetto ha un manager come supervisore e una lista di impiegati coinvolti.

## Design di dettaglio del database
Il database è un componente che inizialmente non avevamo previsto di inserire nel nostro progetto, tuttavia è subito emersa la necessità di adottarlo a causa dei seguenti vantaggi:
- presenza di dati iniziali utili per la GUI &rarr; senza un database ogni volta che si apre la GUI occorre inserire un sacco di istanze manualmente (diversi clienti, diversi impiegati, assegnare conti a clienti...)
- persistenza &rarr; è comodo mantenere/salvare i dati inseriti, in modo che rimangano presenti nell'applicazione nonostante la chiusura della stessa
- facilità di estrazione di dati e parametri utili &rarr; salvando sul database parametri come interessi, fee, tasse e costi si evita di scriverli direttamente nel codice, rendendo la libreria meno rigida.

Il diagramma delle classi UML progettate per creare il database è il seguente:
![uml database](img/UMLDatabase.png)
Dal diagramma emergono i seguenti componenti:
- il trait DatabaseOperations rappresenta una generica tabella presente nel database e le operazioni effettuabili sulle sue righe (inserimento, ricerca, modifica ed eliminazione) &rarr; si noti che tale trait è generico nel tipo T, che specifica il tipo delle entità che popolano la tabella in questione
- esistono poi diverse classi che rappresentano le diverse implementazioni possibili del trait Databaseoperations sopra riportato, che possono essere viste come le differenti tabelle presenti sul database (sicuramente serviranno una tabella per i clienti, una per le  persone, una per gli impiegati, una per gli appuntamenti, una per i conti bancari e una per i tassi d'interesse).

## Design di dettaglio della conversione di valute
Uno dei requisiti emersi in fase di requirements/engineering analysis è offrire funzionalità utili alla conversione di valute, ossia predisporre funzionalità che consentano di effettuare un cambio tra il valore di una moneta e l'equivalente valore di un'altra differente moneta.

Come si nota dal diagramma seguente, i componenti necessari alla conversione di valuta sono diversi:
![uml conversione](img/UMLConversione.png)
- Il trait CurrencyConverter è il convertitore vero e proprio, ovvero il responsabile principale delle funzionalità di conversione di un ammontare di denaro da una valuta ad un'altra &rarr; tra i suoi metodi compaiono anche convertWithFee (per rappresentare che la conversione potrebbe comportare il pagamento di una tassa) ed applyFee che consente di calcolare il valore del denaro dopo l'applicazione della tassa di conversione
- La classe CurrencyConverter è l'implementazione del trait sopra descritto &rarr; si noti che esso contiene tra le altre cose anche un campo privato ExchangeRateProvider che permetterà in qualche modo la conversione di valuta
- Il trait CurrencyConverter usa il trait Currency, che è un'astrazione rappresentante una valuta, con campi simbolo (tipo $, £,..) e codice ufficiale
- La classe CurrencyImpl è l'implementazione del trait sopra descritto
- Infine è presente un object MoneyADT, che mette a disposizione tutte le funzionalità utili a lavorare in modo corretto con valori e quantità monetarie.

[Back to index](../index.md) |
[Previous Chapter](../4-architectural-design/index.md) |
[Next Chapter](../6-implementation/index.md)