package com.kjeldsen.lib;

import com.kjeldsen.lib.filter.handler.ErrorResponseHandler;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseClientApiImpl {

    protected final WebClient webClient;
    protected final InternalClientTokenProvider internalClientTokenProvider;
    protected final ErrorResponseHandler errorResponseHandler = new ErrorResponseHandler();

    private WebClient.ResponseSpec executeRequest(HttpMethod method, String uri,
        @Nullable Map<String, String> headers, @Nullable Object body) {
        String token = internalClientTokenProvider.generateToken("match", "team");

        WebClient.RequestBodySpec requestSpec = webClient
            .method(method)
            .uri(uri)
            .header("Authorization", "Bearer " + token);

        if (headers != null) {
            headers.forEach(requestSpec::header);
        }

        WebClient.RequestHeadersSpec<?> headersSpec = (body != null) ? requestSpec.bodyValue(body) : requestSpec;
        return headersSpec
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, errorResponseHandler.handle4xxErrors())
            .onStatus(HttpStatusCode::is5xxServerError, errorResponseHandler.handle5xxErrors());
    }


    protected WebClient.ResponseSpec executeGetRequest(String uri) {
        return executeRequest(HttpMethod.GET, uri, null, null);
    }

    protected WebClient.ResponseSpec post(String uri, Object body) {
        return executeRequest(HttpMethod.POST, uri, null, body);
    }

    protected <T> T executeRequestMono(String uri, ParameterizedTypeReference<T> responseType) {
        try {
            return executeGetRequest(uri)
                .bodyToMono(responseType)
                .block();
        } catch (Exception e) {
            log.error("Error calling external service at URI: {}", uri, e);
            throw new RuntimeException("Failed to call external service: " + uri, e);
        }
    }

    protected <T> List<T> executeRequestFlux(String uri, ParameterizedTypeReference<T> responseType) {
        return executeGetRequest(uri)
            .bodyToFlux(responseType)
            .collectList()
            .block();
    }

    protected <Req, Res> Res executePostRequest(
        String uri,
        Req requestBody,
        ParameterizedTypeReference<Res> responseType
    ) {
        try {
            return executeRequest(HttpMethod.POST, uri, null, requestBody)
                .bodyToMono(responseType)
                .block();
        } catch(Exception e) {
            throw new RuntimeException("Failed to call external service: " + uri, e);
        }
    }

    protected abstract String buildUri(String... params);
}
