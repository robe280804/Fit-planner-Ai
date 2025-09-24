package com.fit_planner_ai.app.dto;

import com.fit_planner_ai.app.enums.Goal;
import com.fit_planner_ai.app.enums.TrainingLevel;
import com.fit_planner_ai.app.enums.TrainingType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
public class TrainingRequestDto {

    @NotNull(message = "Il numero di giorni non può essere nullo")
    @Min(value = 1, message = "I giorni devono essere almeno 1")
    @Max(value = 7, message = "I giorni non possono superare 7")
    private Integer days;

    @NotNull(message = "La durata non può essere nulla")
    @Min(value = 10, message = "La durata deve essere almeno 10 minuti")
    private Integer duration;

    @NotEmpty(message = "Devi selezionare almeno un tipo di allenamento")
    @Size(max = 4)
    private Set<TrainingType> type;

    @NotEmpty(message = "Devi selezionare almeno un obiettivo")
    @Size(max = 4)
    private Set<Goal> goals;

    @NotNull(message = "Il livello di allenamento è obbligatorio")
    private TrainingLevel trainingLevel;

    @Size(max = 10, message = "Puoi inserire massimo 10 metriche aggiuntive")
    private Map<String, String> additionalMetrics;
}

