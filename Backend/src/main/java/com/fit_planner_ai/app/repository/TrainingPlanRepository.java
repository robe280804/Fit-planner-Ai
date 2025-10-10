package com.fit_planner_ai.app.repository;

import com.fit_planner_ai.app.model.TrainingPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingPlanRepository extends MongoRepository<TrainingPlan, String> {
    List<TrainingPlan> findAllByUserId(UUID userId);

    void deleteByIdAndUserId(String trainingPlanId, UUID userId);
}
