# Requisiti di sistema
Requisiti e specifica (nelle varie tipologie, ossia: 1) business, 2) modello di dominio, 3) funzionali [ 3.1) utente, e 3.2) di sistema ], 4) non funzionali, 5) di implementazione)

Vista la mole di lavoro dietro al progetto, difficile pensare che i requirement occupino meno di 5-6 facciate: siano più sistematici possibile, e quindi fungano da specifica completa (si noti che ogni elemento -- statico/strutturale o dinamico/comportamentale -- di dominio va discusso nei requisiti).
Le scelte tecnologiche non dovrebbero essere anticipate troppo per ovvi motivi: prima le prendete prima impattano tutta la parte successiva e quindi diventano più difficilmente riconsiderabili (comunque in linea di principio ogni scelta ha una sua posizione logica precisa, e potrebbe essere nei requirement, nel design o nell'implementazione, a voi la scelta).
Attenzione in particolare ai requirement non funzionali: 1) non siano troppo vaghi altrimenti sono inverificabili, e quindi praticamente inutili; 2) se il sistema è distribuito, è inevitable dire esattamente cosa vi aspettate (in retrospettiva, cosa ottenete) in termini di di robustezza a cambiamenti/guasti (quali?, come?), e scalabilità (in quale dimensione? fino a che punto?).

Si noti anche che la sezione di "Requisiti e Specifica" deve in modo completo e rigoroso descrivere il funzionamento "esterno" del sistema.

[Back to index](../index.md) | 
[Previous Chapter](../2-development-process/index.md) | 
[Next Chapter](../4-architectural-design/index.md)