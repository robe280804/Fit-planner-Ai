package com.fit_planner_ai.app.dto;

import com.fit_planner_ai.app.enums.Goal;
import com.fit_planner_ai.app.enums.TrainingLevel;
import com.fit_planner_ai.app.enums.TrainingType;
import com.fit_planner_ai.app.model.DailyTraining;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class TrainingPlanDto {

    private Long id;
    private UUID userId;
    private int days;
    private List<DailyTraining> dailyTrainings;
    private Set<TrainingType> type;
    private Set<Goal> goals;
    private TrainingLevel trainingLevel;
}
