# Istruzioni

## Requisiti tecnici 
- Java 11
- Docker 20.10.7
- Docker compose 1.21.2
- Ubuntu 20.04.4 LTS, CPU Ryzen 1500
- Porte 8080 e 5432 libere per l'applicativo in Spring e Postgres

## Fast start
Nello sviluppo dell'applicativo abbiamo usato docker e docker-compose per standardizzare il deploy, le dipendenze 
vengono gestite direttamente da gradle e dal relativo wrapper presente. \
Supponendo di soddisfare i requisiti tecnici per avviare l'applicativo bisogna eseguire i seguenti comandi (supponendo
di trovarsi nella cartella del progetto).

### Build e creazione DockerFile
La creazione del dockerfile è automatica e avviene in fase di build, per poter eseguire la build basta eseguire il
comando `./gradlew dockerCreateDockerFile`, verrà creata la build con le relative dipendenze e il dockerfile da chiamare

### Run
Una volta eseguita la creazione del dockerfile è sufficiente eseguire il comando `docker-compose up` (sempre nella
directory principale). \
Al primo avvio verrà eseguita la build dell'immagine attraverso il dockerfile generato precedentemente.
L'applicativo verrà dunque esposto sulla porta definita (8080 di default).
Per informazioni riguardo gli endpoint e le modalità si guardi il README
