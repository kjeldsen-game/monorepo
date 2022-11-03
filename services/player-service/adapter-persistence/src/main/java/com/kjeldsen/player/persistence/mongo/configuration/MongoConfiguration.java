package com.kjeldsen.player.persistence.mongo.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ConditionalOnProperty(name = "service.persistence.adapter", havingValue = "db")
@EnableMongoRepositories(basePackages = "com.kjeldsen.player.persistence.repositories")
public class MongoConfiguration {
}
