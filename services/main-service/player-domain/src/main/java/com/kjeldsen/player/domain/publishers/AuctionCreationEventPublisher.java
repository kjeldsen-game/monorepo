package com.kjeldsen.player.domain.publishers;


import com.kjeldsen.player.domain.events.AuctionCreationEvent;

public interface AuctionCreationEventPublisher {
    void publishAuctionCreationEvent(AuctionCreationEvent auctionCreationEvent);

}
