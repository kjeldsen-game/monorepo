package com.kjeldsen.match.quartz;

import com.kjeldsen.match.domain.schedulers.JobQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzJobQueryServiceImpl implements JobQueryService {

    private final Scheduler scheduler;

    @Override
    public Optional<ZonedDateTime> getNextFireTime(String jobName, String jobGroup) {
        try {
            JobKey jobKey = new JobKey(jobName, jobGroup);

            if (!scheduler.checkExists(jobKey)) {
                log.warn("Job not found: {}:{}", jobGroup, jobName);
                return Optional.empty();
            }

            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (triggers.isEmpty()) {
                log.warn("No triggers found for job: {}:{}", jobGroup, jobName);
                return Optional.empty();
            }

            Trigger trigger = triggers.get(0);
            if (trigger.getNextFireTime() == null) {
                return Optional.empty();
            }

            ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(
                trigger.getNextFireTime().toInstant(),
                ZoneId.systemDefault()
            );

            return Optional.of(zonedDateTime);
        } catch (SchedulerException e) {
            log.error("Failed to retrieve next fire time for job {}:{}", jobGroup, jobName, e);
            return Optional.empty();
        }
    }
}
