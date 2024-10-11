package com.kjeldsen.integration;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public interface AbstractContainerizedIT {

    String MONGO_VERSION = "mongo:5.0";
    String POSTGRES_VERSION = "postgres:16";

    MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse(MONGO_VERSION));
    PostgreSQLContainer<?> postgresDbContainer = new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_VERSION));

    static void start() {
        mongoDbContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDbContainer.getReplicaSetUrl());
        postgresDbContainer.start();
        System.setProperty("spring.datasource.url", postgresDbContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresDbContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresDbContainer.getPassword());
    }
}
