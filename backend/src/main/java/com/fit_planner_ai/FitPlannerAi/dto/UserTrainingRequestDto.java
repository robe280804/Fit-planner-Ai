package com.fit_planner_ai.FitPlannerAi.dto;

import java.util.Map;

public class UserTrainingRequestDto {

    private Set<Goal> goals;
    private int freeDays;
    private Set<TrainingType> trainingType;
    private List<String> additionalPreferences;
}
