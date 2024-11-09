package com.kjeldsen.integration.events;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.market.application.CreateAuctionUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

public class AuctionCreationEventListener extends AbstractEventIT{

    @MockBean
    private CreateAuctionUseCase createAuctionUseCase;

    @Test
    @DisplayName("Should handle the AuctionEndEvent and check values")
    void should_handle_AuctionCreationEvent_and_check_values() {
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

        assertThat(playerIdCaptor.getValue()).isEqualTo(Player.PlayerId.of("playerId"));
        assertThat(teamIdCaptor.getValue()).isEqualTo(Team.TeamId.of("teamId"));
    }

}
