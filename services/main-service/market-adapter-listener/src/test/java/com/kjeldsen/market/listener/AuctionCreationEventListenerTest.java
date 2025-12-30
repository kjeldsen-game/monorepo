package com.kjeldsen.market.listener;

import com.kjeldsen.lib.events.AuctionCreationEvent;
import com.kjeldsen.market.application.CreateAuctionUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.schedulers.AuctionEndJobScheduler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class AuctionCreationEventListenerTest {

    @Mock
    private CreateAuctionUseCase mockedCreateAuctionUseCase;
    @Mock
    private AuctionEndJobScheduler mockedAuctionEndJobScheduler;
    @InjectMocks
    private AuctionCreationEventListener auctionCreationEventListener;

    @Test
    @DisplayName("Should handle event and process")
    void should_handle_event_and_process() {
        Auction auction = Auction.builder()
            .id(Auction.AuctionId.generate())
            .endedAt(Instant.now().plusSeconds(3600))
            .build();

        Mockito.when(mockedCreateAuctionUseCase.create("player", "team"))
            .thenReturn(auction);

        var event = AuctionCreationEvent.builder()
            .playerId("player")
            .teamId("team")
            .build();

        auctionCreationEventListener.handleAuctionEvent(event);
        Mockito.verify(mockedCreateAuctionUseCase, Mockito.times(1))
            .create("player", "team");
        Mockito.verify(mockedAuctionEndJobScheduler, Mockito.times(1))
            .scheduleAuctionEndJob(
                auction.getId().value(),
                auction.getEndedAt()
            );
    }
}