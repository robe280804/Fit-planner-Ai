package com.fit_planner_ai.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConfig {

    @Bean
    public DefaultErrorHandler errorHandler() {
        // Backoff per i retry del singolo messaggio (qui 0 retry)
        FixedBackOff fixedBackOff = new FixedBackOff(0L, 0L);

        // Gestore dell'errore
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {

            log.error("[KAFKA ERROR HANDLER] Errore nel consumer Kafka per il messaggio: {}", consumerRecord.value(), exception);
            // qui il messaggio viene skip-ato e il consumer continua con il prossimo
        }, fixedBackOff);

        return errorHandler;
    }
}
