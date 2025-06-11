package com.kjeldsen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.mapstruct.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@ActiveProfiles({"test", "test-it"})
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@ContextConfiguration(initializers = MongoDBInitializer.class)
public abstract class AbstractIT {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    static String MONGO_VERSION = "mongo:5.0";

    static MongoDBContainer mongoDbContainer = new MongoDBContainer(DockerImageName.parse(MONGO_VERSION));

    static {
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
