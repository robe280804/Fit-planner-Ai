package com.fit_planner_ai.app.service;

import com.fit_planner_ai.app.dto.TrainingRequestDto;
import com.fit_planner_ai.app.security.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, TrainingRequestDto> kafkaTemplate;

    public void sendRequest(TrainingRequestDto request){
        UUID userId = getUserAuthId();
        request.setUserId(userId);
        log.info("[CONSUMER] Dati inviati dall utente con id {}, [{}]", userId, request);
        kafkaTemplate.send("ai-requests", userId.toString(), request)
                .whenComplete((res, ex) -> {
                    if (ex == null){
                        log.info("[KAFKA PRODUCER] Richiesta inviata con successo a {}", res.getRecordMetadata().topic());
                    } else {
                        log.error("[KAFKA PRODUCER] Errore durante l'invio", ex);
                    }
        });
    }

    private static UUID  getUserAuthId() {
        UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
