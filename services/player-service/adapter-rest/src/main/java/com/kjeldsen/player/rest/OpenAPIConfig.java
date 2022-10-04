package com.kjeldsen.player.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Players Service API",
        version = "1",
        description = "API for the Players Service."
    ),
    servers = {
        @Server(url = "http://localhost:8080")
    }
)
public class OpenAPIConfig {
}
