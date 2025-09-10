package com.fit_planner_ai.FitPlannerAi.controller;

import com.fit_planner_ai.FitPlannerAi.dto.GetTrainerDto;
import com.fit_planner_ai.FitPlannerAi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @PostMapping("/{trainerId}/get-trainer")
    public ResponseEntity<GetTrainerDto> getTrainer(@PathVariable UUID trainerId){
        return ResponseEntity.ok(userService.getTrainer(trainerId));
    }

}
