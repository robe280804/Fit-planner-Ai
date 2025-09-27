package com.fit_planner_ai.app.controller;

import com.fit_planner_ai.app.dto.TrainingRequestDto;
import com.fit_planner_ai.app.service.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training")
@RequiredArgsConstructor
public class TrainingController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/")
    public ResponseEntity<String> createTrainingPlan(TrainingRequestDto request){
        kafkaProducer.sendRequest(request);
        return ResponseEntity.ok("Richiesta inviata a kafka");
    }
}
