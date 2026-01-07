package com.kjeldsen.lib.clients.base;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class UnauthenticatedClientApiImpl extends AbstractClientApiImpl {

    protected UnauthenticatedClientApiImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    protected WebClient.RequestBodySpec prepareRequest(
        org.springframework.http.HttpMethod method,
        String uri
    ) {
        return webClient
            .method(method)
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON);
    }
}
