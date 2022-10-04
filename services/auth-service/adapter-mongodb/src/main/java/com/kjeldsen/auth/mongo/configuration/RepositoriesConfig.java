package com.kjeldsen.auth.persistence.configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.kjeldsen.auth.persistence.repositories")
public class RepositoriesConfig {
}
