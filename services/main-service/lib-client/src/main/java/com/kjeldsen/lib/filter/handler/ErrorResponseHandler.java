package com.kjeldsen.lib.filter.handler;

import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class ErrorResponseHandler {

    public Function<ClientResponse, Mono<? extends Throwable>> handle4xxErrors() {
        return response -> response.bodyToMono(String.class)
            .flatMap(errorBody ->
                Mono.error(new RuntimeException("Client Error: " + errorBody))
            );
    }

    public Function<ClientResponse, Mono<? extends Throwable>> handle5xxErrors() {
        return response -> response.bodyToMono(String.class)
            .flatMap(errorBody ->
                Mono.error(new RuntimeException("Server Error: " + errorBody))
            );
    }
}
