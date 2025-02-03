package com.kjeldsen.match.quartz;

import com.kjeldsen.match.domain.schedulers.MatchScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class MatchSchedulerIImpl implements MatchScheduler {

    private final Scheduler scheduler;

    @Override
    public void scheduleMatch(String matchId, Instant matchDate) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(MatchJob.class)
                .withIdentity("matchJob-" + matchId)
                .usingJobData("matchId", matchId)
                .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("matchTrigger-" + matchId)
                .startAt(Date.from(matchDate))
                .build();

            scheduler.scheduleJob(jobDetail, trigger);

            log.info("Match scheduled for: {} on time {}", matchId, matchDate);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
