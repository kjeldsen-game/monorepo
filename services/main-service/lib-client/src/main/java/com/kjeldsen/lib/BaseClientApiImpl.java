package com.kjeldsen.lib;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public abstract class BaseClientApiImpl {

    protected final WebClient webClient;

    private <T> WebClient.ResponseSpec prepareRequest(String uri) {
        return webClient.get()
            .uri(uri)
            .header("X-Internal-API-Key", "my-secret-key")
            .retrieve()
            .onStatus(HttpStatusCode::is5xxServerError, response ->
                response.bodyToMono(String.class)
                    .flatMap(errorBody ->
                        Mono.error(new RuntimeException("Server error: " + errorBody))
                    )
            );
    }

    protected <T> T executeRequestMono(String uri, ParameterizedTypeReference<T> responseType) {
        return prepareRequest(uri)
            .bodyToMono(responseType)
            .onErrorResume(e -> Mono.empty())
            .block();
    }

    protected <T> List<T> executeRequestFlux(String uri, ParameterizedTypeReference<T> responseType) {
        return prepareRequest(uri)
            .bodyToFlux(responseType)
            .onErrorResume(e -> Flux.empty())
            .collectList()
            .block();
    }

    protected abstract String buildUri(String... params);
}
