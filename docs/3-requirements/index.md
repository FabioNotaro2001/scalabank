# Requisiti e specifica di sistema
<!--Requisiti e specifica (nelle varie tipologie, ossia: 1) business, 2) modello di dominio, 3) funzionali [ 3.1) utente, e 3.2) di sistema ], 4) non funzionali, 5) di implementazione)

Vista la mole di lavoro dietro al progetto, difficile pensare che i requirement occupino meno di 5-6 facciate: siano più sistematici possibile, e quindi fungano da specifica completa (si noti che ogni elemento -- statico/strutturale o dinamico/comportamentale -- di dominio va discusso nei requisiti).
Le scelte tecnologiche non dovrebbero essere anticipate troppo per ovvi motivi: prima le prendete prima impattano tutta la parte successiva e quindi diventano più difficilmente riconsiderabili (comunque in linea di principio ogni scelta ha una sua posizione logica precisa, e potrebbe essere nei requirement, nel design o nell'implementazione, a voi la scelta).
Attenzione in particolare ai requirement non funzionali: 1) non siano troppo vaghi altrimenti sono inverificabili, e quindi praticamente inutili; 2) se il sistema è distribuito, è inevitable dire esattamente cosa vi aspettate (in retrospettiva, cosa ottenete) in termini di di robustezza a cambiamenti/guasti (quali?, come?), e scalabilità (in quale dimensione? fino a che punto?).

Si noti anche che la sezione di "Requisiti e Specifica" deve in modo completo e rigoroso descrivere il funzionamento "esterno" del sistema.-->
Nel presente capitolo sono riportati, suddivisi ed enumerati i requisiti del sistema.

Si noti che, esattamente come prevede la filosofia su cui si basa Scrum, la presente sezione è il frutto di un lavoro iterativo e continuo: l'individuazione e definizione dei requisiti è da conserarsi come un processo più che come un prodotto, in ragione del fatto che i requisiti cambiano col tempo e l'avanzamento dello sviluppo consente di identificarne sempre nuovi.

## Requisiti di business
Tentano di rispondere alla domanda riguardante il percè il progetto ha avuto luogo: esprimono dunque come mai il software è ritenuto straegico, aiutano a definire quali sono gli obiettivi del progetto, le speranze del cliente e degli sviluppatori.

Aiutano inoltre a chiarire e stabilire come giudicare la buona riuscita del progetto.

L'elenco seguente riporta i requisiti di business emersi:
1. il progetto è ritenuto strategico in quanto rappresenta un connubio fondamentale di diversi aspetti universitari &rarr; non solo esso è il mezzo attraverso il quale è possibile sostenere l'esame del corso di PPS, ma esso rappresenta anche una splendida occasione per mettersi alla prova e verificare ed approfondire la comprensione degli aspetti emersi durante le lezioni
2. il progetto prevede la creazione di un framework completo Scala utilizzabile per modellare e produrre applicazioni nel contesto bancario
3. le speranze del team di sviluppo, nonchè gli obiettivi prefissati per poter constatare la buona riuscita del progetto, sono la produzione della libreria in modo chiaro e pulito, non solo nel codice ma anche e soprattutto nella parte di progettazione e strutturazione dei moduli (anche seguendo i principi e best practice affrontate in classe)
4. 
## Requisiti riguardanti la modellazione del dominio
## Requisiti funzionali per l'utente
## Requisiti funzionali di sistema
## Requisiti non funzionali
## Requisiti di implementazione

[Back to index](../index.md) | 
[Previous Chapter](../2-development-process/index.md) | 
[Next Chapter](../4-architectural-design/index.md)