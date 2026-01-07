package com.kjeldsen.lib.clients.base;

import com.kjeldsen.lib.filter.handler.ErrorResponseHandler;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
public abstract class AbstractClientApiImpl {

    protected final WebClient webClient;
    protected final ErrorResponseHandler errorResponseHandler;

    protected AbstractClientApiImpl(WebClient webClient) {
        this.webClient = webClient;
        this.errorResponseHandler = new ErrorResponseHandler();
    }

    protected abstract WebClient.RequestBodySpec prepareRequest(
        HttpMethod method,
        String uri
    );

    protected WebClient.ResponseSpec executeRequest(
        HttpMethod method,
        String uri,
        @Nullable Map<String, String> headers,
        @Nullable Object body) {
        WebClient.RequestBodySpec request = prepareRequest(method, uri);

        if (headers != null) {
            headers.forEach(request::header);
        }

        WebClient.RequestHeadersSpec<?> headersSpec =
            (body != null) ? request.bodyValue(body) : request;

        return headersSpec
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, errorResponseHandler.handle4xxErrors())
            .onStatus(HttpStatusCode::is5xxServerError, errorResponseHandler.handle5xxErrors());
    }

    protected <T> T executeMono(
        Supplier<WebClient.ResponseSpec> request,
        ParameterizedTypeReference<T> type
    ) {
        try {
            return request.get().bodyToMono(type).block();
        } catch (Exception e) {
            throw new RuntimeException("Client call failed", e);
        }
    }

    protected <T> List<T> executeFlux(
        Supplier<WebClient.ResponseSpec> request,
        ParameterizedTypeReference<T> type
    ) {
        try {
            return request.get().bodyToFlux(type).collectList().block();
        } catch (Exception e) {
            throw new RuntimeException("Client call failed", e);
        }
    }
}
