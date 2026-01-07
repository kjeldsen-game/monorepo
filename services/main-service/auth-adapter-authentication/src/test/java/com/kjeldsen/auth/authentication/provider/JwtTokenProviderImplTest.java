package com.kjeldsen.auth.authentication.provider;

import com.kjeldsen.auth.authentication.SecurityProperties;
import com.kjeldsen.auth.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
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
    void should_generate_token() {
        String userId = "test-user-id";
        String teamId = "test-team-id";
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);

        String token = jwtTokenProvider.generateToken(userId, teamId, roles);
        assertNotNull(token);
    }
}