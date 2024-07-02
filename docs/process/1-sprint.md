# Sprint planning
La tabella sottostante riporta lo sprint backlog relativo al primo sprint.
<table>
  <thead>
    <tr>
      <th>PRODUCT BACKLOG ITEM</th>
      <th>SPRINT TASK</th>
      <th>VOLUNTEER</th>
      <th>INITIAL ESTIMATE OF EFFORT</th>
      <th>1</th>
      <th>2</th>
      <th>3</th>
      <th>4</th>
      <th>5</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td rowspan="3">l'utente ha la possibilità di creare e gestire le persone del dominio (personale bancario e clienti)	</td>
      <td>creazione della classe persona</td>
      <td>Bedei, Mazzotti</td>
      <td>2</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>creazione delle classi che rappresentano la gerarchia del personale bancario</td>
      <td>Bedei</td>
      <td>2</td>
      <td>2</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>creazione dei clienti</td>
      <td>Mazzotti</td>
      <td>3</td>
      <td>3</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td rowspan="4">l'utente può usare un prototipo iniziale di GUI</td>
      <td>realizzazione dei mockup delle schermate necessarie</td>
      <td>Bertuccioli</td>
      <td>3</td>
      <td>3</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>realizzazione delle singole schermate</td>
      <td>Bertuccioli</td>
      <td>5</td>
      <td>5</td>
      <td>4</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
    </tr>
    <tr>
      <td>collegamento delle schermate tra loro</td>
      <td>Bertuccioli</td>
      <td>2</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
     <tr>
      <td>implementazione delle funzionalità previste</td>
      <td>Bertucccioli</td>
      <td>4</td>
      <td>4</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td rowspan="5">l'utente può usare un logger per tenere traccia e visualizzare gli eventi accaduti</td>
      <td>individuazione degli eventi rilevanti di cui tenere traccia</td>
      <td>Notaro</td>
      <td>1</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>implementazione generica del logger globale</td>
      <td>Notaro</td>
      <td>2</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>implementazione specializzata del logger per singole entità</td>
      <td>Notaro</td>
      <td>2</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>collegamento con le altre entità del dominio</td>
      <td>Notaro, Bedei, Mazzotti</td>
      <td>2</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>raffinamento ed adozione di design pattern</td>
      <td>Notaro</td>
      <td>2</td>
      <td>2</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
  </tbody>
</table>

# Sprint goal
Come si nota dallo sprint backlog, l'obiettivo principale del primo sprint è non solo predisporre le entità base che serviranno per le implementazioni delle varie funzionalità previste negli sprint successivi, ma contestualmente iniziare la realizzazione di importanti strumenti interattivi (al momento ancora piuttosto limitati) quali il logger e la GUI.

# Terminazione dello sprint
La terminazione dello sprint è avvenuta in data 07/06/2024.

# Sprint review
Il team si ritiene soddisfatto di essere riuscito a completare tutti i task previsti.

Sicuramente occorre lavorare e migliorare le tempistiche, siccome questo primo sprint ha richiesto più tempo del previsto (anche a causa di impegni universitari, lavorativi e personali sopraggiunti).

Ad ogni modo il team si impegna ad essere più rapido nei prossimi sprint.

# Sprint retrospective
La retrospettiva dello sprint ha riguardato principalmente due punti:
- il primo è che si è discussa e valutata una funzionalità che non era stata preventivata, ossia la fidelity del `Customer` (che aumenta e diminuisce in base alle operazioni eseguite dal cliente)
- inoltre si sono discusse alcune regole da seguire per modificare le classi altrui, in modo da evitare che uno sviluppatore si ritrovi una sua classe stravolta da un collega. 