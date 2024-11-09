package com.kjeldsen.match.quartz;

import com.kjeldsen.match.schedulers.MatchScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
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
                .withIdentity("auctionEndTrigger-" + matchId)
                .startAt(Date.from(matchDate))
                .build();

            scheduler.scheduleJob(jobDetail, trigger);

            log.info("Match scheduled for: {} on time {}", matchId, matchId);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
