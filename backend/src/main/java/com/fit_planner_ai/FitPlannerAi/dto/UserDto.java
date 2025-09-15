package com.fit_planner_ai.FitPlannerAi.dto;

import com.fit_planner_ai.FitPlannerAi.enums.AuthProvider;
import com.fit_planner_ai.FitPlannerAi.enums.Roles;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserDto {

    private UUID id;
    private String username;
    private String email;
    private Set<Roles> roles;
    private AuthProvider provider;
    private LocalDateTime createdAt;
}
