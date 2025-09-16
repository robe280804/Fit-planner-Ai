package com.fit_planner_ai.FitPlannerAi.dto;

import com.fit_planner_ai.FitPlannerAi.enums.Goal;
import com.fit_planner_ai.FitPlannerAi.enums.TrainingType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserTrainingRequestDto {

    @NotNull(message = "Devi selezionare almeno un obiettivo")
    @Size(min = 1, message = "Devi selezionare almeno un obiettivo")
    private Set<Goal> goals;

    @Min(value = 1, message = "Devi indicare almeno un giorno disponibile")
    @Max(value = 7, message = "I giorni disponibili non possono superare 7")
    private int freeDays;

    @NotNull(message = "Devi selezionare almeno un tipo di allenamento")
    @Size(min = 1, message = "Seleziona almeno un tipo di allenamento")
    private Set<TrainingType> trainingType;

    @Size(max = 5, message = "Puoi inserire al massimo 5 richieste particolari")
    private List<@NotBlank(message = "Le richieste non possono essere vuote") String> additionalPreferences;
}
