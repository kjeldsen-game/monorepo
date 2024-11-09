package com.kjeldsen.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Main API",
        version = "1"
    ),
    servers = {
        @Server(url = "http://localhost:8081"),
        @Server(url = "https://kjeldsengame.com"),
        @Server(url = "https://actively-accurate-platypus.ngrok-free.app")
    },
    security = {
        @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "oauth2"),
    }
)
@SecuritySchemes({
    @SecurityScheme(
        name = "oauth2",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        flows = @OAuthFlows(
            password = @OAuthFlow(tokenUrl = "/oauth/token", scopes = {@OAuthScope(name = "all", description = "all scope")})
        )
    )
})
public class OpenAPIConfig {
}
