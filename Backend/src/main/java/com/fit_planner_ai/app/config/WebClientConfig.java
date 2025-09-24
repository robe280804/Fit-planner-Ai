package com.fit_planner_ai.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final WebClient webClient;

    @Value("${GEMINI_API_URL}")
    private String geminiUrl;

    @Value("${GEMINI_API_KEY}")
    private String geminiKey;

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl(geminiUrl)
                .defaultHeader("Authorization", "Bearer " + geminiKey)
                .build();
    }
}
