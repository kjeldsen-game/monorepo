package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtTokenProviderImplTest {

    private SecurityProperties mockedSecurityProperties = Mockito.mock(SecurityProperties.class);

    private JwtTokenProviderImpl jwtTokenProvider;
    private String privateKey;

    @BeforeEach
    void setUp() throws Exception {
        // Generate a test RSA key pair
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        when(mockedSecurityProperties.getPrivateKey()).thenReturn(privateKey);
        when(mockedSecurityProperties.getAccessTokenValiditySeconds()).thenReturn(3600L);

        jwtTokenProvider = new JwtTokenProviderImpl(mockedSecurityProperties);
    }

    @Test
    @DisplayName("Should generate token")
    public void should_generate_token() {
        String userId = "test-user-id";
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);

        String token = jwtTokenProvider.generateToken(userId, roles);
        assertNotNull(token);
    }
}