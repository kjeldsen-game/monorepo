package com.kjeldsen.market.persistence.common;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractMongoDbTest {

    static final MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:5.0");

    static {
        mongoDbContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDbContainer.getReplicaSetUrl());
    }
}

