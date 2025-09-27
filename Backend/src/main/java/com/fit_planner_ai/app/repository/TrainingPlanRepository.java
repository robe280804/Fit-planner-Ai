package com.fit_planner_ai.app.repository;

import com.fit_planner_ai.app.model.TrainingPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingPlanRepository extends MongoRepository<TrainingPlan, Long> {
}
