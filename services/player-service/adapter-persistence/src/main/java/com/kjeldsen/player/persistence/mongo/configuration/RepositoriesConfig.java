package com.kjeldsen.player.persistence.mongo.configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.kjeldsen.player.persistence.repositories")
public class RepositoriesConfig {
}
