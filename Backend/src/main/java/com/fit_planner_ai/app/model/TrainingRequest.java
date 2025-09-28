package com.fit_planner_ai.app.model;


import com.fit_planner_ai.app.enums.Goal;
import com.fit_planner_ai.app.enums.TrainingLevel;
import com.fit_planner_ai.app.enums.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("training-request")
public class TrainingRequest {

    @Id
    private String id;
    private UUID userId;
    private Integer days;
    private Integer maxDuration;
    private Set<TrainingType> type;
    private Set<Goal> goals;
    private TrainingLevel trainingLevel;

    private Map<String, String> additionalMetrics;

    @CreatedDate
    private Instant creationAt;
}
