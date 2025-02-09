package com.kjeldsen.market.domain.publishers;

import com.kjeldsen.player.domain.events.BidEvent;

public interface BidEventPublisher {
    void publishBidEndEvent(BidEvent bidEvent);

}
