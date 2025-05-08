package com.kjeldsen.market.domain.publishers;

import com.kjeldsen.lib.events.AuctionEndEvent;

public interface AuctionEndEventPublisher {
    void publishAuctionEndEvent(AuctionEndEvent auctionEndEvent);
}
