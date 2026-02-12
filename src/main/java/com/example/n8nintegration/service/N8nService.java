package com.example.n8nintegration.service;

import com.example.n8nintegration.dto.ErrorLogPayload;
import com.example.n8nintegration.entity.LogEntity;
import com.example.n8nintegration.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class N8nService {

    private final LogRepository logRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${n8n.webhook.url}")
    private String n8nWebhookUrl;

    public void sendToWho(ErrorLogPayload payload) {
        log.info("Sending error payload to n8n: {}", payload);
        webClientBuilder.build()
                .post()
                .uri(n8nWebhookUrl)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> log.info("Successfully sent to n8n: {}", response))
                .doOnError(error -> log.error("Failed to send to n8n", error))
                .subscribe(); // Fire and forget
    }

    public void saveLog(ErrorLogPayload payload) {
        log.info("Saving error log to database: {}", payload);
        LogEntity logEntity = LogEntity.builder()
                .serviceName(payload.service_name())
                .errorMessage(payload.error_message())
                .errorStacktrace(payload.error_stacktrace())
                .timestamp(payload.timestamp())
                .build();
        logRepository.save(logEntity);
    }
}
