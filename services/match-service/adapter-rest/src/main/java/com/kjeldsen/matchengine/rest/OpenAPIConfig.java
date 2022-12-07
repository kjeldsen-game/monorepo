package com.kjeldsen.matchengine.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Policy Service API",
        version = "1",
        description = "API for the Policy Service.",
        contact = @Contact(
            name = "iptiQ Software Engineering Team",
            email = "support@iptiq.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:19002"),
        @Server(url = "http://localhost:8080/policy/v1"),
        @Server(url = "http://localhost/policy/v1"),
        @Server(url = "https://api.{env}.nonprod.iptiq.com/policies/v1", variables = {
            @ServerVariable(name = "env", defaultValue = "dev", description = "The Api Gateway Exposed.",
                allowableValues = {"dev", "sit", "uat", "pat", "anz"})
        })
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenAPIConfig {
}
