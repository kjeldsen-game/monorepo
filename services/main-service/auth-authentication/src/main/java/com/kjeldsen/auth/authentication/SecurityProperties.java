package com.kjeldsen.auth.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
@Getter
@Setter
public class SecurityProperties {

    private long accessTokenValiditySeconds;
    private long refreshTokenValiditySeconds;
    private List<String> publicEndpoints;
    private String publicKey;
    private String privateKey;

    public String getPublicKey() {
        return sanitizePemPublicKey(publicKey);
    }

    public String getPrivateKey() {
        return sanitizePemPrivateKey(privateKey);
    }

    private static String sanitizePemPublicKey(String pem) {
        return pem
            .replaceAll("-----BEGIN PUBLIC KEY-----", "")
            .replaceAll("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
    }

    private static String sanitizePemPrivateKey(String pem) {
        return pem
            .replaceAll("-----BEGIN PRIVATE KEY-----", "")
            .replaceAll("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
    }
}