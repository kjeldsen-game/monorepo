package com.kjeldsen.lib.auth;

import com.kjeldsen.auth.rest.model.ServiceTokenRequest;
import com.kjeldsen.auth.rest.model.TokenResponse;
import com.kjeldsen.lib.clients.AuthClientApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class InternalClientTokenProvider {

    @Value("${internal.api.key}")
    private String clientSecret;
    private final AuthClientApi authClientApi;
    private final ConcurrentHashMap <String, TokenHolder> tokenCache = new ConcurrentHashMap<>();

    public String generateToken(String subject, String audience) {
        long currentTime = System.currentTimeMillis();

        TokenHolder holder = tokenCache.compute(audience, (key, existing) -> {
            if (existing == null || currentTime >= existing.expiresIn) {
                TokenResponse token = authClientApi.getServiceToken(new ServiceTokenRequest()
                    .serviceName(subject)
                    .clientSecret(clientSecret)
                    .audience(audience));
                long expiresAt = currentTime + (5 * 60 * 1000 - 30 * 1000);
                return new TokenHolder(token.getAccessToken(), expiresAt);
            }
            return existing;
        });

        return holder.token;
    }

    @RequiredArgsConstructor
    public static class TokenHolder {
        final String token;
        final long expiresIn;
    }
}
