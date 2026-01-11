package com.kjeldsen.auth.authentication;

import com.nimbusds.jwt.JWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${internal.api.key}")
    private String internalApiKey;

    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring public security filter chain = {}", http);
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
            .authorizeHttpRequests(requests -> requests
                // Public without Authentication
                .requestMatchers(
                    "/v1/auth/token",
                    "/v1/auth/reset-password",
                    "/v1/auth/register",
                    "/v1/auth/forget-password",

                    "/swagger-ui/**",
                    "/api-docs/**",
                    "/actuator/**",
                    "/actuator",
                    "/v1/simulator/hello-world")
                .permitAll()

                // Internal without Authentication
                .requestMatchers("/v1/auth/token-service")
                .access(((authentication, object) ->
                    new AuthorizationDecision(internalApiKey.equals(object.getRequest().getHeader("X-Internal-Request")))))

                // Protected only internal endpoints
                .requestMatchers( HttpMethod.POST, "/v1/league")
                .access(internalAuthorizationManager())

                // Protected public / internal endpoints
                .anyRequest()
                .access(publicOrInternalAuthorizationManager())
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.decoder(jwtDecoder())));
        return http.build();
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> publicOrInternalAuthorizationManager() {
        return (authentication, object) -> {
            if (authentication == null || !(authentication.get().getPrincipal() instanceof Jwt jwt)) {
                return new AuthorizationDecision(false);
            }

            String typeClaim = jwt.getClaimAsString("type");
            boolean isInternalClaim = "internal".equals(typeClaim);

            if (isInternalClaim) {
                return validateInternalRequest(object);
            } else {
                return new AuthorizationDecision(true);
            }
        };
    }

    @Bean
    public AuthorizationManager<RequestAuthorizationContext> internalAuthorizationManager() {
        return (authentication, object) -> {
            if (authentication != null && authentication.get().getPrincipal() instanceof Jwt jwt) {
                String typeClaim = jwt.getClaimAsString("type");
                if ("internal".equals(typeClaim)) {
                    return validateInternalRequest(object);
                }
            }
            return new AuthorizationDecision(false);
        };
    }

    private AuthorizationDecision validateInternalRequest(RequestAuthorizationContext object) {
        String apiKeyHeader = object.getRequest().getHeader("X-Internal-Request");
        return new AuthorizationDecision(internalApiKey.equals(apiKeyHeader));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64
            .getDecoder()
            .decode(securityProperties.getPublicKey()));
        RSAPublicKey publicKey;
        try {
            publicKey = (RSAPublicKey) KeyFactory
                .getInstance("RSA")
                .generatePublic(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            return roles != null ?
                roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toSet()) : Set.of();
        });
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
