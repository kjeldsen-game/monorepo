package com.kjeldsen.integration.events;

import com.kjeldsen.lib.events.AuctionCreationEvent;
import com.kjeldsen.market.application.CreateAuctionUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.schedulers.AuctionEndJobScheduler;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@Disabled

class AuctionCreationEventListenerIT extends AbstractEventIT {

    @MockitoBean
    private CreateAuctionUseCase createAuctionUseCase;
    @MockitoBean
    private AuctionEndJobScheduler auctionEndJobScheduler;

    @Test
    @DisplayName("Should handle the AuctionEndEvent and check values")
    void should_handle_AuctionCreationEvent_and_check_values() {

        Auction auction = Auction.builder()
            .id(Auction.AuctionId.of("auctionId"))
            .teamId("teamId")
            .playerId("playerId")
            .build();

        when(createAuctionUseCase.create("playerId","teamId")).thenReturn(auction);

        com.kjeldsen.lib.events.AuctionCreationEvent auctionCreationEvent = AuctionCreationEvent.builder()
            .teamId("teamId")
            .playerId("playerId")
            .build();

        testEventPublisher.publishEvent(auctionCreationEvent);

        ArgumentCaptor<String> playerIdCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> teamIdCaptor = ArgumentCaptor.forClass(String.class);

        verify(createAuctionUseCase).create(
            playerIdCaptor.capture(),
            teamIdCaptor.capture());

        verify(auctionEndJobScheduler).scheduleAuctionEndJob(
            any(),
            any());

        assertThat(playerIdCaptor.getValue()).isEqualTo("playerId");
        assertThat(teamIdCaptor.getValue()).isEqualTo("teamId");
    }

}
