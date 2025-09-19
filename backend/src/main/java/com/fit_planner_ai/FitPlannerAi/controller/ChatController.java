package com.fit_planner_ai.FitPlannerAi.controller;

import com.fit_planner_ai.FitPlannerAi.dto.UserMessageChatDto;
import com.fit_planner_ai.FitPlannerAi.dto.TrainerMessageChatDto;
import com.fit_planner_ai.FitPlannerAi.service.WebSocketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller //Per websocket, non prevede http response
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketService webSocketService;


    // Allenatore invia messaggio testuale
    @MessageMapping("/chat.trainer.send")
    public void sendTrainerMessage(@Valid TrainerMessageChatDto message) {
        webSocketService.sendToClient(message);
    }

    // Utente invia risposta JSON con specifiche
    @MessageMapping("/chat.user.send")
    public void sendUserMessage(@Valid UserMessageChatDto message) {
        //webSocketService.sendToTrainer(message);
        /*
        messagingTemplate.convertAndSendToUser(
                message.getDestinatario(),  // email dell'allenatore
                "/queue/messages",
                message
        );
        */

    }
}
