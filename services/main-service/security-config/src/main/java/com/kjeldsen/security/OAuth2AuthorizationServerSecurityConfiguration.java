package com.kjeldsen.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OAuth2AuthorizationServerSecurityConfiguration {

    @Value("${security.oauth2.client-id}")
    private String clientId;

    @Value("${security.oauth2.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.public-key}")
    private String publicKey;

    @Value("${security.oauth2.private-key}")
    private String privateKey;

    public static final String[] AUTH_WHITELIST = {
        "/",
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/api-docs/**",
        "/oauth/token"
    };

    public static final String[] AUTH_IGNORED = {
        "/auth/sign-up",
        "/oauth/token"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults())
            .oauth2Client(Customizer.withDefaults())
            .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(AUTH_IGNORED);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration
            .withRegistrationId("default")
            .clientId(clientId)
            .clientSecret(clientSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            //.redirectUri("http://localhost:8081/")
            .redirectUri("https://www.google.com")
            //.redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
            //.scope("openid", "profile", "email", "address", "phone")
            //.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
            .tokenUri("/oauth/token")
            //.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            //.userNameAttributeName(IdTokenClaimNames.SUB)
            //.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .clientName("Default")
            .build();
    }

}
