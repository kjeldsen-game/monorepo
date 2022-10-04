package com.kjeldsen.auth.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
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
        @Server(url = "http://localhost:8081"),
        @Server(url = "http://localhost:8082")
    }
)
public class OpenAPIConfig {
}
