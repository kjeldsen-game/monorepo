package com.kjeldsen.market.quartz;

import com.kjeldsen.lib.events.AuctionEndEvent;
import com.kjeldsen.market.application.AuctionEndUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.publishers.AuctionEndEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEndJob implements Job {

    private final AuctionEndEventPublisher auctionEndEventPublisher;
    private final AuctionEndUseCase auctionEndUseCase;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Ending Auction, time expired, ");
        Auction.AuctionId auctionId = Auction.AuctionId.of(
            jobExecutionContext.getJobDetail().getJobDataMap().getString("auctionId"));

        AuctionEndEvent auctionEndEvent = auctionEndUseCase.endAuction(auctionId);
        auctionEndEventPublisher.publishAuctionEndEvent(auctionEndEvent);
    }
}
