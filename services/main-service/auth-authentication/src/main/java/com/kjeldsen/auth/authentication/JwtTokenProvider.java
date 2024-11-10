package com.kjeldsen.auth.authentication;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final SecurityProperties securityProperties;

    public String generateToken(String userId) {
        try {
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64
                .getDecoder()
                .decode(securityProperties.getPrivateKey()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + securityProperties.getAccessTokenValiditySeconds() * 1000L))
                .signWith(keyFactory.generatePrivate(spec))
                .compact();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
}
