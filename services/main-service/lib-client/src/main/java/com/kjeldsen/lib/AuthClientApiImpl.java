package com.kjeldsen.lib;

import com.kjeldsen.auth.rest.model.ServiceTokenRequest;
import com.kjeldsen.auth.rest.model.TokenResponse;
import com.kjeldsen.lib.clients.AuthClientApi;
import com.kjeldsen.lib.clients.base.UnauthenticatedClientApiImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class AuthClientApiImpl extends UnauthenticatedClientApiImpl implements AuthClientApi {

    public AuthClientApiImpl(WebClient webClient) {
        super(webClient);
    }

    @Override
    public TokenResponse getServiceToken(ServiceTokenRequest request) {
        String uri = UriComponentsBuilder.fromPath("/auth/token-service").toUriString();

        return executeMono(() -> executeRequest(HttpMethod.POST, uri, null, request),
            new ParameterizedTypeReference<>() {});
    }
}
