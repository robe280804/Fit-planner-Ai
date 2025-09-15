package com.fit_planner_ai.FitPlannerAi.dto;

import com.fit_planner_ai.FitPlannerAi.enums.AuthProvider;
import com.fit_planner_ai.FitPlannerAi.enums.Roles;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class RegisterResponseDto {

    private String message;
    private UUID idUtente;
    private String userName;
    private String email;
    private AuthProvider provider;
    private Set<Roles> roles;
}
