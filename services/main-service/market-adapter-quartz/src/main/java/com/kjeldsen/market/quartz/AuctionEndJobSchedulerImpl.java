package com.kjeldsen.market.quartz;

import com.kjeldsen.market.domain.schedulers.AuctionEndJobScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionEndJobSchedulerImpl implements AuctionEndJobScheduler {

    private final Scheduler scheduler;

    @Override
    public void rescheduleAuctionEndJob(final String auctionId, Instant newEndTime) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey("auctionEndTrigger-" + auctionId);

            Trigger oldTrigger = scheduler.getTrigger(triggerKey);
            if (oldTrigger == null) {
                throw new SchedulerException("Trigger not found for auction: " + auctionId);
            }

            Trigger newTrigger = oldTrigger.getTriggerBuilder()
                .startAt(Date.from(newEndTime))
                .build();

            scheduler.rescheduleJob(triggerKey, newTrigger);
            log.info("AuctionEnd rescheduled for: {} from time {} to {}", auctionId, oldTrigger.getEndTime(), newEndTime);

        } catch (SchedulerException e) {
            log.error("Failed to reschedule auction end job for auction {}: {}", auctionId, e.getMessage(), e);
        }
    }


    @Override
    public void scheduleAuctionEndJob(String auctionId, Instant endTime) {

        try {
            JobDetail jobDetail = JobBuilder.newJob(AuctionEndJob.class)
                .withIdentity("auctionEndJob-" + auctionId)
                .usingJobData("auctionId", auctionId)
                .build();
            log.info("{}", endTime);

            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("auctionEndTrigger-" + auctionId)
                .startAt(Date.from(endTime))
                .build();

            // Schedule the job
            scheduler.scheduleJob(jobDetail, trigger);

            log.info("AuctionEnd scheduled for: {} on time {}", auctionId, endTime);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
