package com.fit_planner_ai.app.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Exercise {

    private String name;
    private int series;
    private int repetition;
    private double weight;
    private int restForSeries;
    private String description;
}
