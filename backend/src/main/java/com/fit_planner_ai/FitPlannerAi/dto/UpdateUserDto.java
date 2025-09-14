package com.fit_planner_ai.FitPlannerAi.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateUserDto {

    private String message;
    private UUID idUtente;
    private String userName;
    private String email;
}
