package com.kjeldsen.player.domain.publishers;


import com.kjeldsen.lib.events.AuctionCreationEvent;

public interface AuctionCreationEventPublisher {
    void publishAuctionCreationEvent(AuctionCreationEvent auctionCreationEvent);
}
