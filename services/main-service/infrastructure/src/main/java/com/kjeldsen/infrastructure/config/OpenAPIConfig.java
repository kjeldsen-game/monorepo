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
        @Server(url = "http://localhost:8081")
    },
    security = {
        @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth"),
    }
)
@SecuritySchemes({
    @SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        flows = @OAuthFlows(
            authorizationCode = @OAuthFlow(
                authorizationUrl = "https://kjeldsengame-dev.auth.eu-west-1.amazoncognito.com/oauth2/authorize",
                refreshUrl = "https://kjeldsengame-dev.auth.eu-west-1.amazoncognito.com/oauth2/refresh",
                tokenUrl = "https://kjeldsengame-dev.auth.eu-west-1.amazoncognito.com/oauth2/token",
                scopes = {@OAuthScope(name = "all", description = "all scope")}
            )
        )
    )
})
public class OpenAPIConfig {
}
