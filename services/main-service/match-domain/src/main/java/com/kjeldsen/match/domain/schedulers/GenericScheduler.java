package com.kjeldsen.match.domain.schedulers;

import org.quartz.Job;

import java.time.Instant;
import java.util.Map;

public interface GenericScheduler {

    /**
     * Schedule a job of the given class at a specific time with optional job data.
     *
     * @param jobClass the Quartz Job class to schedule
     * @param jobName  the unique name for the job
     * @param jobGroup optional group name (default can be used)
     * @param runAt    when to run the job
     * @param jobData  optional key-value data to pass to the job
     */
    void scheduleJob(Class<? extends Job> jobClass, String jobName, String jobGroup, Instant runAt, Map<String, Object> jobData);
}
