package com.kjeldsen.market.publisher;

import com.kjeldsen.market.domain.publishers.AuctionEndEventPublisher;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEndEventPublisherImpl implements AuctionEndEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishAuctionEndEvent(AuctionEndEvent auctionEndEvent) {
        log.info("Publishing AuctionEndEvent {}", auctionEndEvent);
        eventPublisher.publishEvent(auctionEndEvent);
    }
}
