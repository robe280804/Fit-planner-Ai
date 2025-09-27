package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.TrainingRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final KafkaTemplate<UUID, TrainingRequestDto> kafkaTemplate;
    private final TrainingService trainingService;

    @KafkaListener(topics = "ai-requests", groupId = "ai-consumer-group")
    public void consumeRequest(TrainingRequestDto request) {
        log.info("[KAFKA CONSUMER] Richiesta arrivata {}", request);
        trainingService.generateTrainingPlan(request);
    }
}
