## 🏋️‍♂️ FitPlanner AI

Descrizione generale:
Applicazione web dove utenti e allenatori interagiscono per creare e seguire piani di allenamento personalizzati, supportati dall’AI.

## 👥 Ruoli e funzionalità principali
🧑‍💻 Utente (trainee)

- 🔑 Registrazione e login (locale o tramite provider esterno come Google/Apple).
- 📊 Inserimento dati personali e corporei: peso, altezza, età, livello di esperienza.
- 🎯 Definizione obiettivi: dimagrimento, aumento massa muscolare, forza, mantenimento.
- 🗂️ Visualizzazione schede di allenamento assegnate dall’allenatore.
- 📈 Aggiornamento progressi: peso, performance e feedback sulle schede.

🏋️ Allenatore

- 🔑 Registrazione e login.
- 🤖 Creazione di piani di allenamento personalizzati usando AI.
- Input dall’utente tramite chat o modulo dinamico (giorni disponibili, tipologia di allenamento, obiettivi).
- L’AI propone una scheda personalizzata.
- ✏️ Modifica manuale delle schede generate dall’AI.
- 📊 Monitoraggio degli utenti e dei loro progressi.

## Struttura Applicazione
🧑‍💻 Utente:
- id: UUID
- username
- email
- password
- ruoli: user, admin
- provider: locale, google, github
- Trainer **L'utente avrà un Trainer, Il Trainer avrà una List<Utente>**

🏋️ Allenatore:
- id: UUID
- username
- email
- password
- ruolo: trainer
- provider: locale, google, github
- List<Utente>
**Possibile descrizione con: tipologia di qualifica, preparazione ...**

🗂️ Scheda allenamento (MongoDB):
- Integer giorni **In una settimana**
- Obbiettivo **forza, massa, esposività ...**
- Livelllo Scheda **principiante, intermedio, avanzato**
- Richieste utente **evitare squat, durata max allenamenti ...**
- List<TrainingDay>

TrainingDay
- giorno **Lunedi**
- durata allenamento
- List<Exercise>

Exercise
- nome 
- Peso **se l'esercizio ne ha bisogno**
- Integer ripetizioni
- Integer serie
- Double recupero
- String breve spiegazione

## Implementazione AI, Kafka e WebSocket
- Kafka per gestire la creazione delle schede in modo asincrono e in grande quantità
- OpenAi o Gemini per la creazione delle schede d'allenamento
- WebSocker per messaggi tra utente e allenatore

## Flusso chat tra Utente e Allenatore
- Allenatore invia messaggio guida: **"text": "Ciao! Per creare la tua scheda ho bisogno di questi dati: obiettivo, 
                                      giorni disponibili, tipi di allenamento, eventuali richieste particolari."**

- L'utente risponde con un JSON strutturato: **{"obiettivo": "Forza",
                                                  "giorniDisponibili": 4,
                                                  "tipiAllenamento": ["Pesi", "Cardio"],
                                                  "richiesteUtente": ["Evitare squat", "Durata max 60 min"]}**

- Allenatore riceve il JSON, e invia la richiesta per creare la scheda all'AI

## EndPoint
🔑 Auth:
- api/auth/register
- api/auth/login

🧑‍💻 User:
- api/user/{trainerId}/get-trainer   

## 🌟 Possibili aggiunte future

📈 Progress Tracking avanzato: grafici per peso, massa muscolare, forza e miglioramenti settimanali.
🔔 Notifiche e reminder: invio di notifiche push o email per allenamenti programmati.
🥗 Consigli nutrizionali personalizzati: generati dall’AI in base agli obiettivi dell’utente.
💬 Community: forum o chat tra utenti per motivazione e scambio di esperienze.
🔬 Analisi avanzata: suggerimenti AI per migliorare performance o correggere errori di allenamento.