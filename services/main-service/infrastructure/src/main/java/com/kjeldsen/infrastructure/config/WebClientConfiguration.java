package com.kjeldsen.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Value("${server.port}")
    private String serverPort;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:" + serverPort + "/v1")
            .build();
    }
}