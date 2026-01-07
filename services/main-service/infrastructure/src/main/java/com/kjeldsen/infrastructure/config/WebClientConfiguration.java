package com.kjeldsen.infrastructure.config;

import com.kjeldsen.lib.BaseClientApiImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class WebClientConfiguration {

    @Value("${server.port}")
    private String serverPort;

    @Value("${internal.api.key}")
    private String internalApiKey;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .baseUrl("http://localhost:" + serverPort + "/v1")
            .defaultHeader("X-Internal-Request",internalApiKey)
            .filter(logRequest())
            .build();
    }

    public ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("➡️ {} {}", request.method(), request.url());
            request.headers().forEach((name, values) ->
                values.forEach(value ->
                    log.info("➡ {}={}", name, value)
                )
            );
            return Mono.just(request);
        });
    }
}