package com.kjeldsen.market.persistence.common;

import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
public abstract class AbstractMongoDbTest {

    static final MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:5.0");

    static {
        mongoDbContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDbContainer.getReplicaSetUrl());
    }
}

