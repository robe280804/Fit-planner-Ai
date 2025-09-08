package com.fit_planner_ai.FitPlannerAi.repository;

import com.fit_planner_ai.FitPlannerAi.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, UUID> {
    boolean existsByEmail( String email);
}
