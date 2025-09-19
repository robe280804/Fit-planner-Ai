package com.fit_planner_ai.FitPlannerAi.config;

import com.fit_planner_ai.FitPlannerAi.security.jwt.JwtService;
import com.fit_planner_ai.FitPlannerAi.security.model.UserDetailsImpl;
import com.fit_planner_ai.FitPlannerAi.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //abilita in Spring Boot il supporto WebSocket con STOMP.
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

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

    /// Configurazione canale inbound, cioè tutti i messaggi che arrivano dal client
    /// - Aggiungo un ChannelInterceptor che mi permette di intercettare e modificare i messaggi prima che arrivino
    /// - preSend permette di leggere, modificare, bloccare...il messaggio prima che arrivi
    /// - StompHeaderAccessor mi permette di incapsulare il msg per ottenere gli header
    /// - L' if controlla che il msg sia un Connect STOMP, perchè l' header può esser letto solo alla registrazione del socket (
    ///     dopo l' handshake non ci sono più gli header)
    /// - Ottengo il token dall' header
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                    String token = accessor.getFirstNativeHeader("Authorization");
                    log.info("[WEBSOCKET] Token ricevuto {}", token);

                    if (token != null && token.startsWith("Bearer ")){
                        token = token.substring(7);
                        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(jwtService.estraiEmail(token));

                        if (jwtService.isTokenValid(token, userDetails)){
                            Authentication auth  = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                            log.info("[WEBSOCKET] Oggetto authentication {}", auth);
                            accessor.setUser(auth);
                        }
                    }
                return message;
            }
        });
    }

}
