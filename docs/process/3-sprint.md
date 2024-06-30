# Sprint Planning
La tabella sottostante riporta lo sprint backlog relativo al terzo ed ultimo sprint.
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
      <td rowspan="2">l'utente può modellare operazioni di risparmio e investimenti tramite il salvadanaio dei clienti</td>
      <td>creazione e modellazione del salvadanaio/risparmi</td>
      <td>Mazzotti</td>
      <td>3</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>creazione e modellazione di investimenti</td>
      <td>Mazzotti</td>
      <td>3</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td rowspan="3">l'utente può modellare operazioni bancarie tra conti (trasferimenti, bonifici...)</td>
      <td>progettazione e creazione dell'entità bonifico</td>
      <td>Notaro</td>
      <td>3</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>aggiunta della funzione che permette ad un cliente di fare un bonifico</td>
      <td>Notaro, Mazzotti</td>
      <td>3</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>collegamento del logger al bonifico</td>
      <td>Notaro</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td rowspan="5">l'utente può modellare prelievi ed operazioni presso un conto corrente</td>
      <td>gestione dei prelievi</td>
      <td>Bedei</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>applicazione di eventuali fee alle operazioni</td>
      <td>Bedei</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>generalizzazione del concetto di fee</td>
      <td>Bedei, Mazzotti</td>
      <td>3</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>gestione dei depositi</td>
      <td>Bedei</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>saldo movimenti del conto</td>
      <td>Bedei, Notaro, Mazzotti</td>
      <td>4</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td rowspan="3">l'utente può interagire con una GUI più completa, che copre gli aspetti rilevanti del dominio</td>
      <td>terminazione della parte della GUI dedicata al login</td>
      <td>Bertuccioli</td>
      <td>1</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>terminazione della parte della GUI dedicata al dipendente</td>
      <td>Bertuccioli, Bedei</td>
      <td>2</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
    <tr>
      <td>terminazione della parte della GUI dedicata al cliente</td>
      <td>Bertuccioli, Bedei, Mazzotti, Notaro</td>
      <td>6</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
      <td>0</td>
    </tr>
  </tbody>
</table>

# Sprint Goal
Come si nota dallo sprint backlog, l'obiettivo principale del terzo ed ultimo sprint è portare a termine il progetto, non solo finendo di implementare i concetti mancanti ma anche inserendoli nella GUI per verificare la correttezza e buona qualità del codice prodotto.

# Terminazione dello sprint
La terminazione dello sprint è avvenuta in data 19/06/2024.

# Sprint Review
Il team si ritiene soddisfatto di essere riuscito a completare tutti i task previsti per questo sprint.

Il team è inoltre molto felice di essere riuscito a migliorare la propria efficienza, ovvero a rispettare il tempo previsto per completare questo sprint.

Una particolarità di questo sprint è stata che due membri del gruppo sono riusciti a portarsi avanti con il lavoro, implementando più entità di quanto previsto.

In particolare Bertuccioli, avendo terminato in fretta l'implementazione delle filiali, si è adoperato per portarsi avanti con l'implementazione anche degli appuntamenti. Come conseguenza di ciò, il suo ultimo sprint prevederà (come si può notare nell'apposita sezione) un impegno per completare la GUI introducendovi le funzionalità necessarie.

Similmente è avvenuto per Bedei che, avendo terminato prima del previsto l'implementazione della conversione di valute, è riuscito nel presente sprint anche ad implementare un database siccome ne è emersa la necessità e comodità per molti concetti (tassi d'interesse, persone...).

# Sprint Retrospective
In ottica di miglioramento ulteriore dell'efficienza e della sincronizzazione del lavoro di gruppo, durante la sprint retrospective si è discusso di probabili ulteriori strategie per evitare i merge conflict.

Inoltre, siccome il progetto si sta avvicinando alla sua deadline e dunque alla parte finale, il gruppo ritiene ragionevole incrementare la frequenza degli incontri (daily scrum giornalieri) per migliorare così la sincronizzazione dei vari flussi di lavoro e fare in modo che ogni membro del team sia maggiormente aggiornato sullo stato dei lavori altrui.