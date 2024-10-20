package com.kjeldsen.market.domain.publishers;

import com.kjeldsen.player.domain.events.AuctionEndEvent;

public interface AuctionEndEventPublisher {
    void publishAuctionEndEvent(AuctionEndEvent auctionEndEvent);
}
