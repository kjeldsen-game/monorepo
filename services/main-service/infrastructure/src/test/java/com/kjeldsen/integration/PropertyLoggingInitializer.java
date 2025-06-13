package com.kjeldsen.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

public class PropertyLoggingInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();
        String mongoUri2 = env.getProperty("spring.data.mongodb.uri");
        System.out.println("📢 Resolved sprint uri from application.yaml: " + mongoUri2);

        String mongoUri = env.getProperty("quartz.properties.org.quartz.jobStore.mongoUri");
        System.out.println("📢 Resolved mongoUri from application.yaml: " + mongoUri);
    }
}
