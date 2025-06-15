package com.kjeldsen.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.stream.Stream;

public class PropertyLoggingInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();

        System.out.println("\n🧩🔍 RESOLVED PROPERTIES DURING CONTEXT INITIALIZATION:");
        Stream.of(
            "spring.data.mongodb.uri",
            "quartz.properties.org.quartz.jobStore.mongoUri",
            "quartz.properties.org.quartz.jobStore.dbName",
            "quartz.properties.org.quartz.jobStore.class",
            "quartz.properties.org.quartz.scheduler.instanceName",
            "quartz.job-store-type"
        ).forEach(key -> {
            String value = env.getProperty(key);
            System.out.printf("➡️ %s = %s%n", key, value);
        });
        System.out.println("🧩🔍 END OF RESOLVED PROPERTIES\n");
    }
}
