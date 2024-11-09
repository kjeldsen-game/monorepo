package com.kjeldsen.player.publisher;

import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import com.kjeldsen.player.domain.publishers.AuctionCreationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionCreationEventPublisherImpl implements AuctionCreationEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishAuctionCreationEvent(AuctionCreationEvent auctionCreationEvent) {
        log.info("Publishing AuctionCreationEvent {}", auctionCreationEvent);
        eventPublisher.publishEvent(auctionCreationEvent);
    }
}
