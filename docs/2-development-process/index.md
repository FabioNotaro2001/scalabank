# Processo di sviluppo adottato
<!--Processo di sviluppo adottato (modalità di divisione in itinere dei task, meeting/interazioni pianificate, modalità di revisione in itinere dei task, scelta degli strumenti di test/build/continuous integration)-->
Nella presente sezione è descritta l'organizzazione generale e la metodologia di sviluppo adottata dal gruppo di lavoro.
# Metodologia di sviluppo
In generale, il processo di sviluppo adottato è di tipo agile e basato su Scrum: più dettagliatamente abbiamo cercato di rispettare la metodologia suggerita dal docente nelle regole d'esame.

Ricordiamo che Scrum è un framework per lo sviluppo di software che segue un approccio agile, iterativo ed incrementale, in cui a ogni iterazione si lavora per implementare nuove funzionalità o migliorare e completare quelle già esistenti nel sistema.

Inoltre occorre evidenziare come Scrum sia fortemente basato su ruoli, artefatti e meeting.

## Panoramica dei ruoli
L'organizzazione tipica prevista dalla metodologia di sviluppo Scrum suggerisce una suddivisione dei ruoli come di seguito riportata:
- committente &rarr; responsabile di garantire l'usabilità, qualità e correttezza del risultato finale &rarr; tale ruolo è stato assegnato a Bertuccioli Giacomo
- product owner &rarr; è il responsabile del progetto, inteso come colui che identifica le caratteristiche chiave del prodotto e definisce le priorità &rarr; all'interno del nostro gruppo tale ruolo è ricoperto da Bedei Andrea
- team di sviluppo &rarr; gruppo cross-funzionale e multi-learning che stima le tempistiche e lavora per costruire il prodotto interagendo con il product owner &rarr; come facile immaginare, tutti i membri del gruppo sono anche membri del team di sviluppo
- scrum master &rarr; esperto di Scrum responsabile di mitigare controversie e supervisionare il rispetto dell'approccio agile durante tutto il ciclo di vita del progetto &rarr; tale ruolo è ricoperto da Notaro Fabio.

## Artefatti
I principali artefatti previsti dall'adozione della metodologia di sviluppo Scrum sono:
- product backlog &rarr; documento preliminare che funge da roadmap/piano di sviluppo globale dell'intero progetto e che riporta una lista che elenca ed esprime la priorità dei diversi task &rarr; gli item presenti sono nuove feature introdotte, miglioramenti, sforzi di ricerca, correzione di difetti ed attività di bootstrap &rarr; è possibile consultare il product backlog relativo al framework [qui](../process/0-product-backlog.md)
- sprint backlog &rarr; è l'equivalente del product backlog ma concentrato solo sui task che si prevede di sviluppare nello sprint in esame &rarr; è possibile consultare le informazioni relative ai vari sprint [qui](../process/index.md).

## Organizzazione dei meeting e degli sprint
Come anticipato a lezione, la metodologia Scrum è fortemente improntata su continue, frequenti e ripetute interazioni tra tutti i diversi ruoli che collaborano alla realizzazione del progetto.

Tali iterazioni, chiamate sprint, saranno svolte con cadenza settimanale. 

Ogni sprint prevede le seguenti attività:
- sprint planning &rarr; riunione preliminare allo sprint nella quale si decidono quali item implementare nel corso dello stesso, si scompongono tali item in sottotask e per ciascuno se ne fornisce una stima approssimativa &rarr; il risultato di tale fase è lo sprint backlog dello sprint corrente
- daily scrum giornalieri &rarr; riunioni quotidiane più o meno brevi utili al team di sviluppo per sincronizzarsi, aggiornarsi sui progressi altrui ed aggiornare i progressi dello sprint backlog
- sprint review &rarr; al termine dello sprint è prevista una riunione del team nella quale si valuta assieme la qualità dei progressi raggiunti
- sprint retrospective &rarr; ulteriore riunione, anche aggregabile a quella precedente, in cui si propongono e discutono variazioni e/o miglioramenti al processo di sviluppo, potenzialmente in grado di migliorarne efficacia, efficienza o tempistiche.

I meeting sopra descritti saranno svolti su piattaforme di videocall note, quali Microsoft Teams e Discord.

## Suddivisione e revisione in itinere dei task
La suddivisione dei task individuata e sotto riportata mira a scomporre le caratteristiche e funzionalità del progetto in parti quanto più possibile bilanciate tra tutti i membri del team di sviluppo:
- conversione di valute, gestione operazioni su conto corrente (prelievi, depositi, saldo...), modellazione personale bancario &rarr; assegnate a Bedei Andrea
- modellazione dell'interfaccia grafica tramite monadi, gestione delle filiali, gestione degli appuntamenti &rarr; assegnate a Bertuccioli Giacomo
- modellazione dei conti e delle sue varianti, modellazione delle diverse tipologie di cliente e gestione salvadanaio &rarr; assegnate a Mazzotti Alex
- modellazione del prospetto mutui, modellazione e gestione trasferimenti tra conti correnti, implementazione logger &rarr; assegnate a Notaro Fabio.

Si noti comunque che l'elenco puntato sopra è solo da intendersi come principale suddivisione delle responsabilità, siccome  il gruppo si impegna a promuovere ed adottare un approccio quanto più collaborativo e cooperativo possibile, senza adottare una distinzione e separazione netta dei compiti.

Sempre secondo l'idea appena descritta, è prevista la possibilità di una revisione in itinere dei task (magari suddividendo tra ulteriori membri del team di sviluppo un task complesso originariamente assegnato ad un solo membro) qualora sopraggiungano condizioni o difficoltà particolari. 

## Definition of done
Ulteriore caratteristica peculiare della metodologia Scrum è la cosiddetta definition of done, ovvero un accordo con cui il gruppo definisce formalmente quando un item/caratteristica/task può considerarsi finito e completato.

Ebbene, il gruppo ha concordato di utilizzare come definition of done il punto dal quale i test non falliscono.

Tale scelta è stata presa anche in ottica di rispetto del principio Scrum che suggerisce una metodologia di sviluppo e progettazione improntata sul test-driven development.

# Test-driven development
Figlio dell'agilità, il TDD è una metodologia di sviluppo software che prevede di avanzare con la fase di testing in maniera contestuale rispetto all'introduzione di nuove feature, in modo da minimizzare e ridurre drasticamente i costi e gli sforzi di manutenzione.

Pertanto il gruppo si impegna a seguire un processo di avanzamaneto e programmazione noto come red-green-refactor, che prevede cicli iterativi di 15-20 minuti composti da:
- fase red &rarr; scrittura di un test che fallisce, in modo da identificare la prossima funzionalità/caratteristica da implementare
- fase green &rarr; scrittura del codice di produzione che faccia passare il test appena definito
- fase refactoring &rarr; ristrutturazione e pulizia del codice.

# Scelta degli strumenti di build automation
L'unico particolare strumento utilizzato è Gradle, noto per automatizzare e semplificare notevolmente il processo di compilazione, build e testing del codice.








[Back to index](../index.md) | 
[Previous Chapter](../1-introduction/index.md) | 
[Next Chapter](../3-requirements/index.md)