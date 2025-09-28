package com.kjeldsen.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
    "com.kjeldsen.match.persistence.mongo.repositories",
    "com.kjeldsen.player.persistence.mongo.repositories",
    "com.kjeldsen.market.persistence.mongo.repositories",
    "com.kjeldsen.auth.persistence.mongo.repositories",
    "com.kjeldsen.notification.persistence.mongo.repositories"
})
public class RepositoriesConfig {
}
