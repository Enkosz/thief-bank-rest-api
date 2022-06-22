# Thief Bank Api
Thief-bank è un applicativo che simula una banca dal punto di vista di un amministratore abbiamo scelto d'implementare \
le funzionalità base indicata all'interno del documento di specifiche. \


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
abbiamo interpretato deposit e withdraw, come delle transfer, aggiungendo il parametro "type" per indicare se è una transfer EXTERNAL, ovvero la transazione, oppure INTERNAL, quali il deposit o il withdraw...
La scelta principale di come si è strutturato il tutto è per renderlo più dinamico possibile ai cambiamenti, riducendo le parti scritte in modo "complicato" ... ci permette di soffrire meno i cambiamenti, anche in corso d'opera, e di aggiornare in modod più veloce e migliore il codice

## Endpoint
Possiamo riassumere gli endpoint in questo modo:
- `{url}:8080/`, restituisce una pagina HTML per ricercare gli account
- `{url}:8080/transfer`, restituisce una pagina HTML per eseguire un trasferimento

Mentre per quanto riguarda le API abbiamo implementato i seguenti endpoint che accettano sia un URL-FORM-ENCODED e JSON:
- `GET {url}:8080/api/account`, restituisce la lista degli account in modo non paginato (per semplicità)
- `GET {url}:8080/api/account/{accountId}`, restituisce le informazioni minimali dato un accountId
- `POST {url}:8080/api/account`, crea un nuovo account, il body deve essere come da specifiche tecniche
- `DELETE {url}:8080/api/account/{accountId}`, esegue il soft delete di un account
- `POST {url}:8080/api/account/{accountId}`, esegue un deposito/prelievo (in base alla quantità) sull'account
- `PUT {url}:8080/api/account/{accountId}`, esegue l'aggiornamento dei dati dell'account come da specifiche
- `PATCH {url}:8080/api/account/{accountId}`, esegue l'aggiornamento in modo univoco come da specifiche
- `HEAD {url}:8080/api/account/{accountId}`, restituisce gli header come da specifiche
- `GET {url}:8080/api/transactions`, restituisce tutte le transazioni di tutti gli utenti, utile per debug
- `POST {url}:8080/api/transfer`, esegue una transazione come da specifiche
- `POST {url}:8080/api/divert`, esegue un revert come da specifiche
