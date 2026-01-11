package com.kjeldsen.lib.clients.base;

import com.kjeldsen.lib.auth.InternalClientTokenProvider;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

public abstract class AuthenticatedClientApiImpl extends AbstractClientApiImpl {

    private final InternalClientTokenProvider internalClientTokenProvider;

    protected AuthenticatedClientApiImpl(
        WebClient webClient,
        InternalClientTokenProvider internalClientTokenProvider
    ) {
        super(webClient);
        this.internalClientTokenProvider = internalClientTokenProvider;
    }

    @Override
    protected WebClient.RequestBodySpec prepareRequest(HttpMethod method, String uri) {
        String token = internalClientTokenProvider.generateToken("match", "team");
        return webClient
            .method(method)
            .uri(uri)
            .header("Authorization", "Bearer " + token);
    }
}
