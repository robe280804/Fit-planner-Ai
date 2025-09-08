package com.fit_planner_ai.FitPlannerAi.dto;

import com.fit_planner_ai.FitPlannerAi.model.AuthProvider;
import com.fit_planner_ai.FitPlannerAi.model.Roles;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class LoginResponseDto {

    private String message;
    private UUID id;
    private String email;
    private AuthProvider provider;
    private Set<Roles> roles;
    private String token;
}
