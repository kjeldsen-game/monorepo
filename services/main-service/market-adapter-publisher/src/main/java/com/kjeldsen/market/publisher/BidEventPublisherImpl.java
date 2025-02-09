package com.kjeldsen.market.publisher;

import com.kjeldsen.market.domain.publishers.BidEventPublisher;
import com.kjeldsen.player.domain.events.BidEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BidEventPublisherImpl implements BidEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishBidEndEvent(BidEvent bidEvent) {
        log.info("Publishing AuctionEndEvent {}", bidEvent);
        eventPublisher.publishEvent(bidEvent);
    }
}
