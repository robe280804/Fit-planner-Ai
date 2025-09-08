package com.fit_planner_ai.FitPlannerAi.mapper;

import com.fit_planner_ai.FitPlannerAi.dto.RegisterResponseDto;
import com.fit_planner_ai.FitPlannerAi.model.BaseUser;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public RegisterResponseDto registerResponseDto(BaseUser user){
        return RegisterResponseDto.builder()
                .message("Registrazione avvenuta con successo")
                .idUtente(user.getId())
                .email(user.getEmail())
                .provider(user.getProvider())
                .roles(user.getRoles())
                .build();
    }
}
