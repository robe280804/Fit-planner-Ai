package com.fit_planner_ai.app.model;

import com.fit_planner_ai.app.enums.Goal;
import com.fit_planner_ai.app.enums.TrainingLevel;
import com.fit_planner_ai.app.enums.TrainingType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Document("training-plan")
public class TrainingPlan {

    @Id
    private Long id;

    private UUID userId;
    private int days;
    private List<DailyTraining> dailyTrainings;
    private Set<TrainingType> type;
    private Set<Goal> goals;
    private TrainingLevel trainingLevel;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updateAt;
}
