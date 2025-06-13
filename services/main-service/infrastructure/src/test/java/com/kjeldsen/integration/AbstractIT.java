package com.kjeldsen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.ClassRule;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@ActiveProfiles({"test", "test-it"})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(initializers = PropertyLoggingInitializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractIT {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    static String MONGO_VERSION = "mongo:5.0";

    @Container
    static MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse(MONGO_VERSION));

    static {
        System.out.println("Starting MongoDB container");
        mongoDbContainer.start();
    }

    @DynamicPropertySource
    static void registerMongoProperties(DynamicPropertyRegistry registry) {
        System.out.println("Registering MongoDB container");
        System.out.println("MongoDB container: " + mongoDbContainer.getContainerIpAddress());
        System.out.println("MongoDB container: " + mongoDbContainer.getReplicaSetUrl());

        System.out.println(mongoDbContainer.getConnectionString());
        System.out.println(mongoDbContainer.getHost());
        System.out.println(mongoDbContainer);
        System.out.println(mongoDbContainer.getPortBindings());
        registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
        registry.add("quartz.properties.org.quartz.jobStore.mongoUri", mongoDbContainer::getReplicaSetUrl);
    }
}
