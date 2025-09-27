package com.fit_planner_ai.app.mapper;

import com.fit_planner_ai.app.dto.TrainingPlanDto;
import com.fit_planner_ai.app.model.TrainingPlan;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    public TrainingPlanDto trainingPlanDto(TrainingPlan trainingPlan){
        return TrainingPlanDto.builder()
                .id(trainingPlan.getId())
                .userId(trainingPlan.getUserId())
                .days(trainingPlan.getDays())
                .dailyTrainings(trainingPlan.getDailyTrainings())
                .type(trainingPlan.getType())
                .goals(trainingPlan.getGoals())
                .trainingLevel(trainingPlan.getTrainingLevel())
                .build();
    }
}
