package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.TrainingRequestDto;
import com.fit_planner_ai.app.model.TrainingPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {

    @Value("${GEMINI_API_URL}")
    private String geminiUrl;

    @Value("${GEMINI_API_KEY}")
    private String geminiKey;

    private final WebClient webClient;


    public String generetePrompt(TrainingRequestDto request) {
        return String.format("""
                        Genera un JSON che rappresenti un oggetto TrainingPlan basato sui dati forniti dall'utente.
                        
                        La struttura del TrainingPlan deve essere esattamente questa:
                        {
                            "days": int,
                            "dailyTrainings": [
                                {
                                    "trainingDuration": int,
                                    "exercises": [
                                        {
                                            "name": String,
                                            "series": int,
                                            "repetition": int,
                                            "weight": double,
                                            "restForSeries": int
                                        }
                                    ]
                                }
                            ],
                            "type": Set<TrainingType>,
                            "goals": Set<Goal>,
                            "trainingLevel": TrainingLevel
                        }
                        
                        Specifiche:
                        1. "days": %d rappresenta i giorni in una settimana di allenamento; genera tanti dailyTrainings quanti sono i giorni.
                        2. "trainingDuration": deve rispettare il "maxDuration" di %d minuti; distribuisci gli esercizi in modo realistico all'interno di questa durata.
                        3. "type": %s rappresenta le tipologie di allenamento (es. forza, ipertrofia, cardio); genera esercizi coerenti con questi tipi.
                        4. "goals": %s rappresenta gli obiettivi dell'utente (es. aumento massa, forza, resistenza); scegli esercizi che li supportino.
                        5. "trainingLevel": %s indica il livello dell'utente (principiante, intermedio, avanzato); adatta serie, ripetizioni e carichi di conseguenza.
                        6. "additionalMetrics": %s contiene indicazioni aggiuntive dell'utente (es. evitare squat, nessun bilanciere); rispettale nella generazione degli esercizi.
                        
                        Genera un JSON coerente, realistico e pronto da parsare in un oggetto TrainingPlan. Mantieni tutti i campi obbligatori e assicurati che i valori siano coerenti e realistici.
                        """,
                request.getDays(),
                request.getDuration(),
                request.getType(),
                request.getGoals(),
                request.getTrainingLevel(),
                request.getAdditionalMetrics()
        );
    }
}
