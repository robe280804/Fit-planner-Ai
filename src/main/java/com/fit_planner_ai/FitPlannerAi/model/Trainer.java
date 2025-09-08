package com.fit_planner_ai.FitPlannerAi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "trainers")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Trainer extends BaseUser{

    @OneToMany(mappedBy = "trainer")
    private List<User> clients;

    //**Possibile descrizione con: tipologia di qualifica, preparazione ...**
}
