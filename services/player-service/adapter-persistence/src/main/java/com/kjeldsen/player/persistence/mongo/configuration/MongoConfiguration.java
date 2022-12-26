package com.kjeldsen.player.persistence.mongo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.kjeldsen.player.persistence.mongo.repositories")
@Configuration
public class MongoConfiguration {
}
