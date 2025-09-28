package com.fit_planner_ai.app.controller;

import com.fit_planner_ai.app.dto.TrainingPlanDto;
import com.fit_planner_ai.app.dto.TrainingRequestDto;
import com.fit_planner_ai.app.service.KafkaProducer;
import com.fit_planner_ai.app.service.TrainingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final KafkaProducer kafkaProducer;
    private final TrainingService trainingService;

    @PostMapping("/")
    public ResponseEntity<String> createTrainingPlan(@RequestBody @Valid TrainingRequestDto request){
        kafkaProducer.sendRequest(request);
        return ResponseEntity.ok("Richiesta inviata con successo!");
    }

    @GetMapping("/")
    public ResponseEntity<List<TrainingPlanDto>> getTrainingPlans(){
        return ResponseEntity.ok(trainingService.getTrainingPlans());
    }

    @DeleteMapping("/{trainingPlanId}")
    public ResponseEntity<Boolean> delete(@PathVariable String trainingPlanId){
        return ResponseEntity.ok(trainingService.delete(trainingPlanId));
    }
}
