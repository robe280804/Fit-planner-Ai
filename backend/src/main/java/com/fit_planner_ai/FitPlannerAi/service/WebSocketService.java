package com.fit_planner_ai.FitPlannerAi.service;

import com.fit_planner_ai.FitPlannerAi.dto.TrainerMessageChatDto;
import com.fit_planner_ai.FitPlannerAi.model.Trainer;
import com.fit_planner_ai.FitPlannerAi.model.User;
import com.fit_planner_ai.FitPlannerAi.repository.TrainerRepository;
import com.fit_planner_ai.FitPlannerAi.repository.UserRepository;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    public void sendToClient(TrainerMessageChatDto message, UserDetailsImpl userDetails) {

        log.info("user {}", userDetails.getEmail());
        String email = userDetails.getEmail();
        String trainerEmail = message.getDestinatario();
        log.info("[WEBSOCKET] User {} sta inviando un messaaggio all'alllenatore {}", email, trainerEmail);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        Trainer trainer = trainerRepository.findByEmail(trainerEmail)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato"));

        if (!user.getTrainer().equals(trainer)){
            throw new RuntimeException("");
        }
        messagingTemplate.convertAndSendToUser(
                trainerEmail,
                "/queue/messages",
                message
        );
    }

    private UUID getAuthUserId(){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }
}
