# Thief Bank Api
Thief-bank è un applicativo che simula una banca dal punto di vista di un amministratore abbiamo scelto d'implementare le funzionalità base indicata all'interno del documento di specifiche. 

## Componenti
- 872783 Giannino Simone
- 866147 Biotto Simone

## Architettura
Abbiamo scelto di utilizzare Spring Boot come Framework in Java per rappresentare la web application. \
Per cercare di ottenere una suddivisione ottimale e dunque per rispettare il SRP abbiamo diviso le responsabilità
delle classi all'interno dell'applicazione nel seguente modo:
- controllers, contengono sia i web controller che servono le pagine statiche e anche i rest controller per le API
- services, per rappresentare la business logic all'interno dell'applicazione
- repository, persistence layer usando principalmente Spring Data
- entity, per rappresentare il dominio del problema
- dto e mappers, per uniformare la rappresentazione delle API dalla logica di business e del dominio

Per quanto riguarda il frontend abbiamo deciso di utilizzare un approccio minimale, usando JS puro per il layer API con
AJAX. \
Per ridurre la complessità abbiamo usato anche JQuery, Bootstrap e Datatables.
Le pagine vengono esposte in modo statico sempre dal backend. \
Le varie eccezioni vengono gestiti tramiti codici univoci che possono essere interpretati dal frontend in modo libero

## Scelte
* Abbiamo intepretato un deposito o un prelievo come una transazione, da e verso lo stesso account. L'abbiamo differenziato da una transazione normale tramite un parametro `type`:
  - `EXTERNAL`: in questo caso si parla di una transazione da e verso sè stessi è una transazione normale
  - `INTERNAL`: invece in questo caso si parla di un prelievo o di un deposito

  Dal punto di vista del frontend, i due tipi sono differenziati, nel primo caso il mittente e il destinatario saranno mostrati e saranno lo stesso, mentre nel seconod caso il mittente e il destinatario non saranno mostrati.

* Inoltre, un'altra scelta fatta è stato trattare le `divert` delle transazioni, come delle semplici transazioni, questo porta alla possibilità di effettuare una `revert` di una `revert` di una transazione.
* La scelta principale di come si è strutturato il tutto è per renderlo più dinamico possibile ai cambiamenti, riducendo le parti scritte in modo "complicato" , questo ci permette di soffrire meno i cambiamenti, anche in corso d'opera, e di aggiornare in modo più veloce e di effettuare refactoring del codice molto più velocemente. Infatti, abbiamo diviso in due sezioni l'applicativo, dividendo i controller, services e repository nei due oggetti del dominio, quali le transazioni e l'account.

## Endpoint
Possiamo riassumere gli endpoint in questo modo:
- `{url}:8080/`, restituisce una pagina HTML per ricercare gli account
- `{url}:8080/transfer`, restituisce una pagina HTML per eseguire un trasferimento

Mentre per quanto riguarda le API abbiamo implementato i seguenti endpoint che accettano sia un URL-FORM-ENCODED e JSON:
- `GET {url}:8080/api/account`, restituisce la lista degli account in modo non paginato (per semplicità)
- `GET {url}:8080/api/account/{accountId}`, restituisce le informazioni minimali dato un accountId
- `POST {url}:8080/api/account`, crea un nuovo account, il body deve essere come da specifiche tecniche
- `DELETE {url}:8080/api/account`, esegue il soft delete di un account
- `POST {url}:8080/api/account/{accountId}`, esegue un deposito/prelievo (in base alla quantità) sull'account
- `PUT {url}:8080/api/account/{accountId}`, esegue l'aggiornamento dei dati dell'account come da specifiche
- `PATCH {url}:8080/api/account/{accountId}`, esegue l'aggiornamento in modo univoco come da specifiche
- `HEAD {url}:8080/api/account/{accountId}`, restituisce gli header come da specifiche
- `GET {url}:8080/api/transactions`, restituisce tutte le transazioni di tutti gli utenti, utile per debug
- `POST {url}:8080/api/transfer`, esegue una transazione come da specifiche
- `POST {url}:8080/api/divert`, esegue un revert come da specifiche
