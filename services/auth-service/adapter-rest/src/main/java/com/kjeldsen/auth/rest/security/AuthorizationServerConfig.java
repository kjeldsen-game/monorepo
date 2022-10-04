package com.kjeldsen.auth.rest.security;

import com.kjeldsen.auth.domain.SignUp;
import com.kjeldsen.auth.usecases.UserDetailsServiceUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    public static final String SCOPE_READ = "read";
    public static final String SCOPE_WRITE = "write";
    public static final String SCOPE_ALL = "all";
    public static final String AUTHORIZED_GRANT_TYPE_PASSWORD = "password";
    public static final String AUTHORIZED_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    public static final String TOKEN_ENHANCE_USER_ID = "uid";

    @Qualifier("authenticationManagerBean")
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceUseCase userDetailsServiceUseCase;

    @Value("${security.oauth2.access-token-validity-seconds}")
    private Integer accessTokenValiditySeconds;

    @Value("${security.oauth2.refresh-token-validity-seconds}")
    private Integer refreshTokenValiditySeconds;

    @Value("${security.oauth2.client-id}")
    private String clientId;

    @Value("${security.oauth2.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.public-key}")
    private String publicKey;

    @Value("${security.oauth2.private-key}")
    private String privateKey;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {

        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {

            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, final OAuth2Authentication authentication) {

                final SignUp signUp = (SignUp) userDetailsServiceUseCase.loadUserByUsername(authentication.getName());

                final Map<String, Object> additionalInfo = new HashMap<>();
                additionalInfo.put(TOKEN_ENHANCE_USER_ID, signUp.getId());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

                accessToken = super.enhance(accessToken, authentication);
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(new HashMap<>());
                return accessToken;
            }

            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                OAuth2Authentication auth = super.extractAuthentication(map);
                auth.setDetails(map);
                return auth;
            }
        };

        converter.setSigningKey(privateKey);
        converter.setVerifierKey(publicKey);

        return converter;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("permitAll()")
            .allowFormAuthenticationForClients();
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsServiceUseCase)
            .tokenStore(tokenStore())
            .tokenEnhancer(accessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
            .inMemory()
            .withClient(clientId)
            .secret(passwordEncoder.encode(clientSecret))
            .scopes(SCOPE_READ, SCOPE_WRITE, SCOPE_ALL)
            .authorizedGrantTypes(AUTHORIZED_GRANT_TYPE_PASSWORD, AUTHORIZED_GRANT_TYPE_REFRESH_TOKEN)
            .accessTokenValiditySeconds(accessTokenValiditySeconds)
            .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
    }

}
