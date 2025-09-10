package com.fit_planner_ai.FitPlannerAi.mapper;

import com.fit_planner_ai.FitPlannerAi.dto.GetTrainerDto;
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
}
