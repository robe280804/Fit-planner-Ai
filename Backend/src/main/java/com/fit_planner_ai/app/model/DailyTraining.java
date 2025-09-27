package com.fit_planner_ai.app.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DailyTraining {

    private int trainingDuration;
    private List<Exercise> exercises;
}
