package com.kjeldsen.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public interface AbstractContainerizedIT {

    String MONGO_VERSION = "mongo:5.0";

    MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse(MONGO_VERSION));

    static void start() {
        mongoDbContainer.start();
    }

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("Registering MongoDB container");
        System.out.println("MongoDB container: " + mongoDbContainer.getContainerIpAddress());
        System.out.println("MongoDB container: " + mongoDbContainer.getReplicaSetUrl());
        registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
    }
}