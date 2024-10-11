package com.kjeldsen.db.migration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {
    "com.kjeldsen.player.persistence.mongo.repositories",
})
public class RepositoriesConfig {
}
