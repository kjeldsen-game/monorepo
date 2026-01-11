package com.kjeldsen.integration.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@TestConfiguration
public class QuartzTestConfig {

    @Autowired
    private MongoDBContainer mongoDbContainer;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        Properties props = new Properties();
        props.setProperty("org.quartz.jobStore.class", "com.novemberain.quartz.mongodb.MongoDBJobStore");
        props.setProperty("org.quartz.jobStore.mongoUri", mongoDbContainer.getReplicaSetUrl());
        props.setProperty("org.quartz.jobStore.dbName", "quartz_db");
        props.setProperty("org.quartz.jobStore.collectionPrefix", "quartz_");

        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setQuartzProperties(props);
        return factoryBean;
    }
}
