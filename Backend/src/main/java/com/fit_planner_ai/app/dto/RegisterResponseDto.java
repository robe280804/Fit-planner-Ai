package com.fit_planner_ai.app.dto;

import com.fit_planner_ai.app.enums.AuthProvider;
import com.fit_planner_ai.app.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class RegisterResponseDto {

    private String message;
    private UUID idUtente;
    private String name;
    private String surname;
    private String email;
    private AuthProvider provider;
    private Set<Role> roles;
}
