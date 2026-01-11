package com.kjeldsen.integration.config;

import com.kjeldsen.integration.AbstractContainerizedIT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MongoDBContainer;

@Configuration
public class TestContainersConfig {

    @Bean(destroyMethod = "stop")
    public MongoDBContainer mongoDbContainer() {
        MongoDBContainer container = AbstractContainerizedIT.mongoDbContainer;
        if (!container.isRunning()) {
            container.start();
        }
        return container;
    }
}
