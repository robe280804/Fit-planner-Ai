package com.fit_planner_ai.FitPlannerAi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseUser {

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
}
