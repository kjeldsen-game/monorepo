package com.kjeldsen.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
    "com.kjeldsen.auth",
    "com.kjeldsen.match",
    "com.kjeldsen.player.persistence.mongo.repositories"
})
public class RepositoriesConfig {
}
