package com.fit_planner_ai.FitPlannerAi.service;

import com.fit_planner_ai.FitPlannerAi.dto.TrainerMessageChatDto;
import com.fit_planner_ai.FitPlannerAi.model.BaseUser;
import com.fit_planner_ai.FitPlannerAi.model.Trainer;
import com.fit_planner_ai.FitPlannerAi.model.User;
import com.fit_planner_ai.FitPlannerAi.repository.BaseUserRepository;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.security.Principal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final BaseUserRepository baseUserRepository;

    /// Aggiungere in futuro l'autenticazione nella chat, in modo da rimuovere id e basarsi sull'utente autenticato
    public void sendToClient(TrainerMessageChatDto message) {
        UUID userId = (UUID) message.getUserId();
        String email = message.getFrom();
        log.info("Allenatore con id {} e email {}", userId, email);

        String clientEmail = message.getDestinatario();
        UUID clientId = (UUID) message.getDestinatarioId();
        log.info("Cliente con id {} e email {}", clientId, clientEmail);

        Trainer trainer = trainerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con email " + email));

        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con email " + clientEmail));

        if (!user.getTrainer().getId().equals(trainer.getId())){
            throw new RuntimeException("");
        }

        log.info("[WEBSOCKET] Messaggio [{}] inviato al cliente {}", message.getContenuto(), clientEmail);
        messagingTemplate.convertAndSendToUser(
                clientEmail,
                "/queue/messages",
                message
        );
    }

    private UUID getAuthUserId(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
