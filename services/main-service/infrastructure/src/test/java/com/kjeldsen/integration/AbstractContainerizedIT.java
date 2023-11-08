package com.kjeldsen.integration;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public interface AbstractContainerizedIT {

    String MONGO_VERSION = "mongo:5.0";

    MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse(MONGO_VERSION));

    static void start() {
        mongoDbContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDbContainer.getReplicaSetUrl());
    }

}
