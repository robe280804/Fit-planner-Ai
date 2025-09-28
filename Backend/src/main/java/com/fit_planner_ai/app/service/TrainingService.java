package com.fit_planner_ai.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit_planner_ai.app.dto.TrainingPlanDto;
import com.fit_planner_ai.app.dto.TrainingRequestDto;
import com.fit_planner_ai.app.enums.Goal;
import com.fit_planner_ai.app.enums.TrainingLevel;
import com.fit_planner_ai.app.enums.TrainingType;
import com.fit_planner_ai.app.exception.AiResponseParsingException;
import com.fit_planner_ai.app.mapper.TrainingMapper;
import com.fit_planner_ai.app.model.DailyTraining;
import com.fit_planner_ai.app.model.Exercise;
import com.fit_planner_ai.app.model.TrainingPlan;
import com.fit_planner_ai.app.model.TrainingRequest;
import com.fit_planner_ai.app.repository.TrainingPlanRepository;
import com.fit_planner_ai.app.repository.TrainingRequestRepository;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainingService {

    private final AiService aiService;
    private final TrainingPlanRepository trainingPlanRepository;
    private final TrainingRequestRepository trainingRequestRepository;
    private final TrainingMapper trainingMapper;

    /**
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Ottengo il DTO inviado dall'utente attraverso kafka </li>
     *     <li> Genero il prompt da inviare a gemini </li>
     *     <li> Eseguo la chiamata a gemini, e converto la risposta nella classe TrainingPlan </li>
     * </ul>
     * @param request DTO con i dati inviati dall'utente
     * @return DTO che rappresenta la scheda d'allenamento generata
     */
    public TrainingPlanDto generateTrainingPlan(TrainingRequestDto request) {
        String prompt = generetePrompt(request);
        String response = aiService.getAiAnswer(prompt);
        return convertAiResponse(response, request);
    }

    /**
     * <p> Il metodo esegue i seguenti step: </p>
     * <ul>
     *     <li> Estraggo il testo puro dalla risposta AI (textNode) </li>
     *     <li> Pulizia da eventuali blocchi Markdown (cleanedJson) </li>
     *     <li> Estraggo i nodi principali e per ognuno creo le classi concrete </li>
     *     <li> Salvo il trainPlan generato e la trainingRequest inviata dall'utente nel database </li>
     *     <li> Ritorno un DTO della scheda d'allenamento creata </li>
     * </ul>
     * @param response dell'ai
     * @param request inviat dall'utente al producer kafka
     * @return Dto che rappresenta la scheda d'allenamento
     * @throws AiResponseParsingException
     */
    private TrainingPlanDto convertAiResponse(String response, TrainingRequestDto request) {
        log.info("[TRAINING PLAN] Conversione della risposta generata da gemini [{}]", response);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode textNode = rootNode
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            if (textNode.isMissingNode()){
                throw new RuntimeException("Errore, contenuto del testo mancante");
            }

            String cleanedJson = textNode.asText()
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("```", "")
                    .trim();

            JsonNode internalRoot = mapper.readTree(cleanedJson);
            JsonNode trainingCardsNode = internalRoot.path("trainingCards");
            JsonNode dailyTrainingsNode = trainingCardsNode.get("dailyTrainings");

            List<DailyTraining> dailyTrainings = new ArrayList<>();

            for (JsonNode dayTrainingNode : dailyTrainingsNode){
                DailyTraining dailyTraining = new DailyTraining();
                dailyTraining.setDay(dayTrainingNode.get("day").asText());
                dailyTraining.setTrainingDuration(dayTrainingNode.get("trainingDuration").asInt());
                List<Exercise> exercises = new ArrayList<>();

                for (JsonNode exerciseNode : dayTrainingNode.get("exercises")){
                    Exercise newExercise = new Exercise();
                    newExercise.setName(exerciseNode.get("name").asText());
                    newExercise.setSeries(exerciseNode.get("series").asInt());
                    newExercise.setRepetition(exerciseNode.get("repetition").asInt());
                    newExercise.setWeight(exerciseNode.get("weight").asDouble());
                    newExercise.setRestForSeries(exerciseNode.get("restForSeries").asInt());
                    newExercise.setDescription(exerciseNode.get("description").asText());

                    exercises.add(newExercise);
                }
                dailyTraining.setExercises(exercises);
                dailyTrainings.add(dailyTraining);
            }

            TypeReference<Set<TrainingType>> trainingTypeSetType = new TypeReference<>() {};
            TypeReference<Set<Goal>> goalSetType = new TypeReference<>() {};

            log.info("TrainingCrdNodes: {}", trainingCardsNode);
            log.info("DailyCardNode: {}", dailyTrainingsNode);

            TrainingPlan trainingPlan = TrainingPlan.builder()
                    .userId(request.getUserId())
                    .days(trainingCardsNode.get("days").asInt())
                    .dailyTrainings(dailyTrainings)
                    .type(mapper.convertValue(trainingCardsNode.get("type"),  trainingTypeSetType))
                    .goals(mapper.convertValue(trainingCardsNode.get("goals"), goalSetType))
                    .trainingLevel(TrainingLevel.valueOf(trainingCardsNode.get("trainingLevel").asText()))
                    .build();

            TrainingRequest trainingRequest = TrainingRequest.builder()
                    .userId(request.getUserId())
                    .days(request.getDays())
                    .maxDuration(request.getDuration())
                    .type(request.getType())
                    .goals(request.getGoals())
                    .trainingLevel(request.getTrainingLevel())
                    .additionalMetrics(request.getAdditionalMetrics())
                    .build();

            TrainingPlan savedTrainingPlan = trainingPlanRepository.save(trainingPlan);
            trainingRequestRepository.save(trainingRequest);

            log.info("[TRAINING PLAN] Training plan creato correttamente [{}]", savedTrainingPlan);
            return trainingMapper.trainingPlanDto(savedTrainingPlan);

        } catch (JsonProcessingException e) {
            log.error("[TRAINING PLAN] Errore nel parsing della risposta AI");
            throw new AiResponseParsingException("Errore nel parsing della risposta AI", e);
        }
    }

    private String generetePrompt(TrainingRequestDto request) {
        return String.format("""
                        Genera un JSON che rappresenti un oggetto TrainingPlan basato sui dati forniti dall'utente.
                        
                        La struttura del TrainingPlan deve essere esattamente questa:
                        {
                          "trainingCards": {
                            "days": int,
                            "dailyTrainings": [
                              {
                                "day": String
                                "trainingDuration": int,
                                "exercises": [
                                  {
                                    "name": String,
                                    "series": int,
                                    "repetition": int,
                                    "weight": double,
                                    "restForSeries": int,
                                    "description": String
                                  }
                                ]
                              }
                            ],
                            "type": Set<TrainingType>,
                            "goals": Set<Goal>,
                            "trainingLevel": TrainingLevel
                          }
                        }
                        
                        Specifiche:
                        1. "days": %d rappresenta i giorni in una settimana di allenamento; genera tanti dailyTrainings quanti sono i giorni.
                        2. "trainingDuration": deve rispettare il "maxDuration" di %d minuti; distribuisci gli esercizi in modo realistico all'interno di questa durata.
                        3. "type": %s rappresenta le tipologie di allenamento (es. forza, ipertrofia, cardio); genera esercizi coerenti con questi tipi.
                        4. "goals": %s rappresenta gli obiettivi dell'utente (es. aumento massa, forza, resistenza); scegli esercizi che li supportino.
                        5. "trainingLevel": %s indica il livello dell'utente (principiante, intermedio, avanzato); adatta serie, ripetizioni e carichi di conseguenza.
                        6. "additionalMetrics": %s contiene indicazioni aggiuntive dell'utente (es. evitare squat, nessun bilanciere); rispettale nella generazione degli esercizi.
                        
                        Genera un JSON coerente, realistico e pronto da parsare in un oggetto TrainingPlan. 
                        Mantieni tutti i campi obbligatori e assicurati che i valori siano coerenti e realistici.
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
