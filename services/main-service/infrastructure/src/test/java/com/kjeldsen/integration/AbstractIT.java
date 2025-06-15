package com.kjeldsen.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = TestApplication.class)
@Testcontainers
@ActiveProfiles({"test-it"})
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = PropertyLoggingInitializer.class)
@Import({TestContainersConfig.class, QuartzTestConfig.class})
public abstract class AbstractIT implements AbstractContainerizedIT {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public MockMvc mockMvc;

    static {
        AbstractContainerizedIT.start();
    }
}