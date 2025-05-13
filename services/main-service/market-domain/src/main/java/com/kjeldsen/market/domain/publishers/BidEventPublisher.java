package com.kjeldsen.market.domain.publishers;

import com.kjeldsen.lib.events.BidEvent;

public interface BidEventPublisher {
    void publishBidEndEvent(BidEvent bidEvent);

}
