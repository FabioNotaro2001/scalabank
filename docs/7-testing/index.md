# Testing

## Approccio di testing perseguito
Il testing è stato un processo del progetto a cui abbiamo cercato di dare il giusto peso e la corretta attenzione.

Come suggeritoci durante il corso e anticipato nella precedenti sezioni della relazione, il team di sviluppo ha cercato di adottare quanto più possibile il TDD, non solo perchè esso risulta essere particolarmente agile, ma anche e soprattutto siccome è uno strumento comprovato che migliora notevolmente la qualità del osftware sviluppato (infatti ricordiamo che il TDD è un importante strumento per sviluppare il codice, non soloper testarlo).

Appurati tutti i vantaggi teorici del TDD che ci sono stati presentati a lezione, il team ha cercato quanto più possibile di applicarlo (in particolare la variante con cicli red-green-refactor ripetuti e continui).

Come autovalutazione, occorre precisare che talvolta il TDD non è stato applicato correttamente o addirittura non è stato proprio applicato, a causa ad esempio del componente stesso di codice da testare (il caso più emblematico è forse quello della GUI, che poco si presta ad essere realizzata seguendo il TDD, ma che comunque ha subito attenti test di usabilità e correttezza degli input forniti).

Come risultato dei nostri sforzi di testing sono stati scritti (e superati) un totale di 183 test:
![testing](img/testing.png)

I vari test sono stati suddivisi cercando di coprire tutti i principali componenti del sistema, infatti esistono test per verificare la correttezza dei concetti di:
- banca
- salvadanaio
- currency
- database &rarr; a sua volta suddivisibili in test per la tabella degli appuntamenti, dei conti correnti, dei tipi di banca, delle currency, dei clienti, degli impiegati, dei tassi d'interesse, dei movimenti e delle persone
- appuntamento
- cliente
- impiegato
- manager
- persona
- operazione bancaria (deposito, prelievo e bonifico)
- mutuo
- logger

e molti altri.

## Tecnologie di testing utilizzate
Tra le tecnologie di testing utilizzate occorre menzionare:
- Scalatest &rarr; libreria flessibile e molto popolare per il testing di codice sviluppato in Scala
- Scoverage &rarr; strumento gratuito per verificare la code coverage in Scala (i suoi report sono commentati nella sezione seguente).

## Grado di copertura del testing
Come anticipato nella sezione precedente, il tool Scoverage ci ha permesso di ottenere un report contenente la percentuale di linee di codice testate.

L'immagine seguente riporta il risultato ottenuto:

Cercate di dare una idea di quanto pensate che i vostri test automatizzati coprano il codice e dove: è importante per stimare il potenziale impatto di una modifica al software.
![testing](img/coverage1.png)
Il report mostra una copertura di codice di poco superiore al 52%.

Tale risultato sembra essere molto deludente tuttavia, come accennato in precedenza, su tale percentuale pesa molto il codice usato per implementare la GUI (che infatti ha una copertura del 2% pur rappresentando quasi un terzo delle linee totali di codice).

In altre parole la sola GUI, che non è stata testata a fondo e seguendo il TDD per ovvi motivi, abbassa drasticamente la percentuale di test coverage siccome rappresenta circa un terzo del totale delle linee di codice della libreria.

Per avere un risultato più preciso e che tenga conto solo del framework è stato possibile ignorare tutto il codice presente nel package della GUI, ottenenndo un risultato decisamente migliore, riportato nell'immagine sottostante:
![testing](img/coverage2.png)

La nuova ed ottima percentuale dell'89% ci sembra maggiormente indicativa degli sforzi perseguiti nel tentativo di seguire l'approccio consigliato dal TDD ed è senza dubbio un buon segno, molto significativo, circa la qualità del lavoro svolto e del codice sviluppato.

Il tool Scoverage offre tra l'altro anche un report che descrive più in dettaglio la test coverage a livello dei singoli componenti:
![testing](img/coverage3.png)

In generale siamo molto soddisfatti dei vantaggi che ci ha apportato l'approccio TDD, tra i quali ci preme evidenziare:
- migliore qualità del codice
- migliore leggibilità e facilità di refactor del codice
- rilevazione anticipata di bug, errori ed imprecisioni
- migliore comprensione dei requisiti
- collaborazione facilitata a causa della miglior documentazione prodotta
- sviluppo di software maggiormente prevedibile
- riduzione dei technical debt.

Oltre ai vantaggi sopra riportati che abbiamo potuto avvertire e confermare in prima persona grazie al progetto, il team si ritiene soddisfatto anche anche della percentuale di test coverage raggiunta, siccome ci aspettavamo una coverage molto minore (intorno al 75%).
[TORNA ALL'INDICE](../index.md) |
[PRECEDENTE CAPITOLO](../6-implementation/index.md) |
[PROSSIMO CAPITOLO](../8-retrospettiva/index.md)