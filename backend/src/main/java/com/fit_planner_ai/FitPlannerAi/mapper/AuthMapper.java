package com.fit_planner_ai.FitPlannerAi.mapper;

import com.fit_planner_ai.FitPlannerAi.dto.LoginResponseDto;
import com.fit_planner_ai.FitPlannerAi.dto.RegisterResponseDto;
import com.fit_planner_ai.FitPlannerAi.model.BaseUser;
import com.fit_planner_ai.FitPlannerAi.enums.Roles;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthMapper {

    public RegisterResponseDto registerResponseDto(BaseUser user){
        return RegisterResponseDto.builder()
                .message("Registrazione avvenuta con successo")
                .idUtente(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .provider(user.getProvider())
                .roles(user.getRoles())
                .build();
    }

    public LoginResponseDto loginResponseDto(UserDetailsImpl userDetails, String token){
        return LoginResponseDto.builder()
                .message("Login effettuato con successo")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .provider(userDetails.getProvider())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .map(role -> Roles.valueOf(role.replace("ROLE_", "")))
                        .collect(Collectors.toSet()))
                .token(token)
                .build();
    }
}
