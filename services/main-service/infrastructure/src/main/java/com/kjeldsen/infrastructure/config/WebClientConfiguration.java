package com.kjeldsen.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("!test & !test-it")
public class WebClientConfiguration {

    @Value("${SERVER_PORT:15001}")
    private String serverPort;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:" + serverPort + "/v1")
            .build();
    }
}