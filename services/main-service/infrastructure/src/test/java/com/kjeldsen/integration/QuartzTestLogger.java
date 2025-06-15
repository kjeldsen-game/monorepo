package com.kjeldsen.integration;

import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class QuartzTestLogger {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void logQuartzProperties() throws SchedulerException {
        System.out.println("Logging Quartz properties:");
        scheduler.getContext().forEach((k, v) -> System.out.println(k + " = " + v));
    }
}
