## 🏋️‍♂️ FitPlanner AI

- In sviluppo, sono possibili cambiamenti

Descrizione generale:
Applicazione web dove utenti possono creare e personalizzare piani di allenamento, supportati dall’AI.

## 👥 Ruoli e funzionalità principali
🧑‍💻 Utente (trainee)

- 🔑 Registrazione e login (locale o tramite provider esterno come Google/Apple).
- 📊 Inserimento dati personali e corporei: peso, altezza, età, livello di esperienza.
- 🎯 Definizione obiettivi: dimagrimento, aumento massa muscolare, forza, mantenimento.
- 🤖 Creazione di piani di allenamento personalizzati usando AI.
- 🗂️ Visualizzazione schede di allenamento.
- 📈 Aggiornamento progressi: peso, performance...


## Struttura Applicazione
🧑‍💻 Utente:
- **id**: UUID
- **username**
- **email**
- **password**
- **ruoli**: user, admin
- **provider**: locale, google, github

🗂️ Scheda allenamento (MongoDB):
- **Integer giorni** In una settimana
- **Obbiettivo** forza, massa, esposività ...
- **Livelllo Scheda** principiante, intermedio, avanzato
- **Richieste utente** evitare squat, durata max allenamenti ...
- **List<TrainingDay>**

TrainingDay
- **giorno** Lunedi
- **durata allenamento**
- **List<Exercise>**

Exercise
- **nome**
- **Peso** se l'esercizio ne ha bisogno
- **Integer ripetizioni**
- **Integer serie**
- **Double recupero**
- **String breve spiegazione**

## Implementazione AI, Kafka
- **Kafka** per gestire la creazione delle schede in modo asincrono e in grande quantità
- **OpenAi** o Gemini per la creazione delle schede d'allenamento


## Flusso Autenticazione
- L'utente si registra da provider **Locale** o esterno come **GitHub** e **Google**
- L'utente può eseguire il Login solo se si è registrato in **locale**
- Generazione **access token** e **refresh token** sia per l'accesso locale che da provider esterno
- L'access token viene salvato nel **local storage**, il refresh token in un **cookie**
- Ad **ogni richiesta** viene inviato l'**access token**
- Se scaduto, viene inviato dal frontend il refresh token che, se valido, genera un nuovo **access token**
- Se anche il **refresh** token è **scaduto** l'utente viene **reindirizzato al login**

## EndPoint
🔑 Auth:
- api/auth/register
- api/auth/login

🧑‍💻 User:
-
-

## 🛠️ Stack Tecnologico

Backend:

- **Java + Spring Boot**: logica di business e gestione API REST.
- **Spring Security + JWT**: autenticazione sicura e gestione utenti.
- **MySQL**: gestione dati principali come utenti e allenatori.
- **MongoDB**: gestione schede di allenamento e strutture flessibili come TrainingDay ed Exercise.
- **Kafka**: gestione asincrona della creazione schede e notifiche.
- **OpenAI / Gemini API**: generazione automatica dei piani di allenamento tramite AI.

Frontend:

- **React**: costruzione dell’interfaccia web interattiva basata su componenti.
- **TailwindCSS**: stile responsive alle pagine
- **React Router Dom**: per la navigazione tra le pagine.


## 🌟 Possibili aggiunte future