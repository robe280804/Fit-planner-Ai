package com.fit_planner_ai.FitPlannerAi.repository;

import com.fit_planner_ai.FitPlannerAi.model.BaseUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, UUID> {
    boolean existsByEmail(String email);

    BaseUser findByEmail(String trainerEmail);
}
