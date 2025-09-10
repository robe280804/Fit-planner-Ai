package com.fit_planner_ai.FitPlannerAi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

/**
 * Entità Trainer
 * Rappresenta un utente che è un allenatore, con ruolo TRAINER
 */
@Entity
@Table(name = "trainers")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
public class Trainer extends BaseUser{

    /**
     * Un allenatore ha una lista di utenti (user)
     */
    @OneToMany(mappedBy = "trainer")
    private List<User> clients;


    //**Possibile descrizione con: tipologia di qualifica, preparazione ...**
}
