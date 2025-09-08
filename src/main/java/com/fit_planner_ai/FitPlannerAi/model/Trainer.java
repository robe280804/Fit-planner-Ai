package com.fit_planner_ai.FitPlannerAi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Entità Trainer
 * Rappresenta un utente che è un allenatore, con ruolo TRAINER
 */
@Entity
@Table(name = "trainers")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Trainer extends BaseUser{

    /**
     * Un allenatore ha una lista di utenti (user)
     */
    @OneToMany(mappedBy = "trainer")
    private List<User> clients;

    //**Possibile descrizione con: tipologia di qualifica, preparazione ...**
}
