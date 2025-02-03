package com.kjeldsen.infrastructure.config;

import com.kjeldsen.player.quartz.NewDayJob;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class QuartzConfiguration {

    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void setupQuartz() {
        try {
            // TODO Quartz fix the interval in minutes, set to minute while testing
            scheduleJob(NewDayJob.class, "newDayJob", "newDayJob",
                SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInMinutes(2)  //617 10.28 hours = 617 minutes
                    .repeatForever()
                    .withMisfireHandlingInstructionFireNow());
//
//            scheduleJob(NewWeekJob.class, "newWeekJob", "newWeekTrigger",
//                SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInHours(72)  // One week in Kjeldsen is 3 days in real life
//                    .repeatForever()
//                    .withMisfireHandlingInstructionFireNow());
//
//
//            scheduleJob(EndSeasonJob.class, "endSeasonJob", "endSeasonTrigger",
//                SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInHours(13 * 7 * 24) // 13 weeks in real life
//                    .repeatForever()
//                    .withMisfireHandlingInstructionFireNow());
//
//            scheduleJob(NewSeasonJob.class, "newSeasonJob", "newSeasonTrigger",
//                SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInHours(13 * 7 * 24 + 1) // 13 weeks in real life -> one hour after seasonEnd
//                    .repeatForever()
//                    .withMisfireHandlingInstructionFireNow());

//            scheduleJob(TestJob.class, "testJob", "testJob",
//                SimpleScheduleBuilder.simpleSchedule()
//                    .withIntervalInMinutes(1)  //617 10.28 hours = 617 minutes
//                    .repeatForever()
//                    .withMisfireHandlingInstructionFireNow());

        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }


    private void scheduleJob(Class<? extends Job> jobClass, String jobName,
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
