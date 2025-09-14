package com.fit_planner_ai.FitPlannerAi.mapper;

import com.fit_planner_ai.FitPlannerAi.dto.GetTrainerDto;
import com.fit_planner_ai.FitPlannerAi.dto.UpdateUserDto;
import com.fit_planner_ai.FitPlannerAi.dto.UserDto;
import com.fit_planner_ai.FitPlannerAi.model.BaseUser;
import com.fit_planner_ai.FitPlannerAi.model.Trainer;
import com.fit_planner_ai.FitPlannerAi.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public GetTrainerDto getTrainerDto(Trainer trainer, User user){
        return GetTrainerDto.builder()
                .message("Complimenti, sei un nuovo cliente di " + trainer.getUserName())
                .userId(user.getId())
                .userEmail(user.getEmail())
                .trainerId(trainer.getId())
                .trainerEmail(trainer.getEmail())
                .build();
    }

    public UpdateUserDto updateUserDto(BaseUser user) {
        return UpdateUserDto.builder()
                .message("Utente aggiornato con successo")
                .idUtente(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }

    public UserDto userDto(BaseUser user){
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
