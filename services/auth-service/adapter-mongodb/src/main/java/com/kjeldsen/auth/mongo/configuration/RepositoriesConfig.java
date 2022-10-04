package com.kjeldsen.auth.mongo.configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.kjeldsen.auth.mongo.repositories")
public class RepositoriesConfig {
}
