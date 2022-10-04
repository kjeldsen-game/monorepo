package com.kjeldsen.auth.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Auth Service API",
        version = "1",
        description = "API for the Auth Service."
    ),
    servers = {
        @Server(url = "http://localhost:8081")
    }
)
@SecurityScheme(
    name = "security_auth",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(password = @OAuthFlow(
        tokenUrl = "/oauth/token",
        refreshUrl = "/oauth/token",
        scopes = {@OAuthScope(name = "all", description = "all scope")}))
)
public class OpenAPIConfig {
}
