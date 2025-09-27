package com.fit_planner_ai.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiService {

    @Value("${GEMINI_API_URL}")
    private String geminiUrl;

    @Value("${GEMINI_API_KEY}")
    private String geminiKey;

    private final WebClient webClient;

    public String getAiAnswer(String prompt){
        Map<String, Object> request = Map.of(
            "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                    })
                });

        try {
            return webClient.post()
                    .uri(geminiUrl + geminiKey.trim())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Errore chiamata Gemini", e);
            return null;
        }
    }
}
