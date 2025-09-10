package com.fit_planner_ai.FitPlannerAi.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

/**
 * Entità User
 * Rappresenta un utente classico nel sistema, con ruolo USER
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class User extends BaseUser {

    /**
     * Un utente ha un allenatore
     */
    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;



}
