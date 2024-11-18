package com.kjeldsen.integration.events;

import com.kjeldsen.market.application.CreateAuctionUseCase;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.schedulers.AuctionEndJobScheduler;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuctionCreationEventListenerIT extends AbstractEventIT {

    @MockBean
    private CreateAuctionUseCase createAuctionUseCase;
    @MockBean
    private AuctionEndJobScheduler auctionEndJobScheduler;

    @Test
    @DisplayName("Should handle the AuctionEndEvent and check values")
    void should_handle_AuctionCreationEvent_and_check_values() {

        Auction auction = Auction.builder()
            .id(Auction.AuctionId.of("auctionId"))
            .teamId(Team.TeamId.of("teamId"))
            .playerId(Player.PlayerId.of("playerId"))
            .build();

        when(createAuctionUseCase.create(Player.PlayerId.of("playerId"), Team.TeamId.of("teamId"))).thenReturn(auction);

        AuctionCreationEvent auctionCreationEvent = AuctionCreationEvent.builder()
            .teamId(Team.TeamId.of("teamId"))
            .playerId(Player.PlayerId.of("playerId"))
            .build();

        testEventPublisher.publishEvent(auctionCreationEvent);

        ArgumentCaptor<Player.PlayerId> playerIdCaptor = ArgumentCaptor.forClass(Player.PlayerId.class);
        ArgumentCaptor<Team.TeamId> teamIdCaptor = ArgumentCaptor.forClass(Team.TeamId.class);

        verify(createAuctionUseCase).create(
            playerIdCaptor.capture(),
            teamIdCaptor.capture());

        verify(auctionEndJobScheduler).scheduleAuctionEndJob(
            any(),
            any());

        assertThat(playerIdCaptor.getValue()).isEqualTo(Player.PlayerId.of("playerId"));
        assertThat(teamIdCaptor.getValue()).isEqualTo(Team.TeamId.of("teamId"));
    }

}
