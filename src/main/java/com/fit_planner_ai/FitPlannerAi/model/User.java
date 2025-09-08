package com.fit_planner_ai.FitPlannerAi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entità User
 * Rappresenta un utente classico nel sistema, con ruolo USER
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseUser {

    /**
     * Un utente ha un allenatore
     */
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;
}
