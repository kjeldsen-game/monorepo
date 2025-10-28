package com.kjeldsen.match.quartz;

import com.kjeldsen.match.domain.schedulers.GenericScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenericSchedulerImpl implements GenericScheduler {

    private final Scheduler scheduler;

    @Override
    public void scheduleJob(Class<? extends Job> jobClass, String jobName, String jobGroup, Instant runAt, Map<String, Object> jobData) {
        try {
            if (jobGroup == null || jobGroup.isBlank()) {
                jobGroup = "default";
            }

            JobBuilder jobBuilder = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup);

            if (jobData != null && !jobData.isEmpty()) {
                JobDataMap dataMap = new JobDataMap(jobData);
                jobBuilder.usingJobData(dataMap);
            }

            JobDetail jobDetail = jobBuilder.build();

            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "Trigger", jobGroup)
                .startAt(Date.from(runAt))
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Scheduled job {} in group {} at {}", jobName, jobGroup, runAt);
        } catch (SchedulerException e) {
            log.error("Failed to schedule job {}: {}", jobName, e.getMessage(), e);
        }
    }
}
