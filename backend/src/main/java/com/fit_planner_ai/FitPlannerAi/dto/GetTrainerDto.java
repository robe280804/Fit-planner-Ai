package com.fit_planner_ai.FitPlannerAi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class GetTrainerDto {

    private String message;
    private UUID userId;
    private String userEmail;
    private UUID trainerId;
    private String trainerEmail;

}
