package com.kjeldsen.market.listener;

import com.kjeldsen.market.application.CreateAuctionUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.schedulers.AuctionEndJobScheduler;
import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionCreationEventListener {

    private final CreateAuctionUseCase createAuctionUseCase;
    private final AuctionEndJobScheduler auctionEndJobScheduler;

    @EventListener
    public void handleAuctionEvent(AuctionCreationEvent auctionCreationEvent) {
        log.info("AuctionCreationEvent received: {}", auctionCreationEvent);
        // Create a auction and store it
        Auction auction = createAuctionUseCase.create(auctionCreationEvent.getPlayerId(), auctionCreationEvent.getTeamId());
        // Schedule auction end
        auctionEndJobScheduler.scheduleAuctionEndJob(auction.getId().value(), auction.getEndedAt());
    }
}
