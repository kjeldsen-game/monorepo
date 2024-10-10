package com.kjeldsen.infrastructure.config;

import com.kjeldsen.player.rest.quartzTestTODORemove.EveryMinuteJob;
import com.kjeldsen.player.rest.quartzTestTODORemove.NewWeekJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@Slf4j
public class QuartzConfiguration {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void setupQuartz() {
        try {
            // Schedule EveryMinuteJob
//            scheduleJob(EveryMinuteJob.class, "everyMinuteJob", "everyMinuteTrigger",
//                    SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10).repeatForever());

            scheduleJob(NewWeekJob.class, "newWeekJob", "newWeekTrigger",
                    SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInMinutes(1)  // Schedule to run every minute
                            .repeatForever());         // Repeat indefinitely

//            scheduleJob(NewWeekJob.class, "newWeekJob", "newWeekTrigger",
//                    CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInWeeks(1));

//            // Schedule NewSeasonJob
//            scheduleJob(NewSeasonJob.class, "newSeasonJob", "newSeasonTrigger",
//                    CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMonths(3));

        } catch (SchedulerException e) {
            e.printStackTrace();  // Handle exceptions
        }
    }


    private void scheduleJob(Class <? extends Job> jobClass, String jobName,
         String triggerName, ScheduleBuilder<?> scheduleBuilder) throws SchedulerException {

        JobKey jobKey = new JobKey(jobClass.getName());
        if (!scheduler.checkExists(jobKey)) {
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName)
                .startNow()
                .withSchedule(scheduleBuilder)
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            log.info("Job with key '{}' already exists.", jobName);
        }
    }
}
