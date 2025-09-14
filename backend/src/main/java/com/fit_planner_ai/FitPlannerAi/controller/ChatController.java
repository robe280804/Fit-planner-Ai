package com.fit_planner_ai.FitPlannerAi.controller;

import com.fit_planner_ai.FitPlannerAi.dto.UserMessageChatDto;
import com.fit_planner_ai.FitPlannerAi.dto.TrainerMessageChatDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller //Per websocket, non prevede http response
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;


    // Allenatore invia messaggio testuale
    @MessageMapping("/chat.trainer.send")
    public void sendTrainerMessage(@Valid TrainerMessageChatDto message) {
        messagingTemplate.convertAndSendToUser(
                message.getDestinatario(),  // email del cliente
                "/queue/messages",
                message
        );
    }

    // Utente invia risposta JSON con specifiche
    @MessageMapping("/chat.user.send")
    public void sendUserMessage(@Valid UserMessageChatDto message) {
        messagingTemplate.convertAndSendToUser(
                message.getDestinatario(),  // email dell'allenatore
                "/queue/messages",
                message
        );
    }
}
