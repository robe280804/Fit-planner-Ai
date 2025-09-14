package com.fit_planner_ai.FitPlannerAi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //abilita in Spring Boot il supporto WebSocket con STOMP.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /// Configurazione Broker
    ///
    /// - topic: broadcast pubblico, chiunque è registrato riceve il messaggio
    /// - queue: chat privata
    /// - app: per i messaggi tra client -> server
    /// - user: messaggi privati per destinatario specifico (/user/{username}/queue/messages)
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    /// Configurazione Endpoint
    /// - chat: endpoint a cui connettersi
    /// - richieste consentite da qualsiasi origine
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

}
