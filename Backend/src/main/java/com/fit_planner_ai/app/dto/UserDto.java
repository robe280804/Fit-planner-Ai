package com.fit_planner_ai.app.dto;

import com.fit_planner_ai.app.enums.AuthProvider;
import com.fit_planner_ai.app.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserDto {

    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private AuthProvider provider;
    private Set<Role> roles;
}
