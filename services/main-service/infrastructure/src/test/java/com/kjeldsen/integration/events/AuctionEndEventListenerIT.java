package com.kjeldsen.integration.events;

import com.kjeldsen.domain.EventId;
import com.kjeldsen.player.application.usecases.ProcessPlayerTransferUseCase;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionEndEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

public class AuctionEndEventListenerIT extends AbstractEventIT{

    @MockBean
    private ProcessPlayerTransferUseCase mockedProcessPlayerTransferUseCase;

    @Test
    @DisplayName("Should handle the AuctionEndEvent and check values")
    void should_handle_AuctionEndEvent_and_check_values() {
        AuctionEndEvent testAuctionEndEvent = AuctionEndEvent.builder()
            .auctionCreator(Team.TeamId.of("auctionCreator"))
            .auctionWinner(Team.TeamId.of("auctionWinner"))
            .id(EventId.from("eventId"))
            .playerId(Player.PlayerId.of("playerId"))
            .amount(BigDecimal.TEN)
            .build();

        testEventPublisher.publishEvent(testAuctionEndEvent);

        ArgumentCaptor<Player.PlayerId> playerIdCaptor = ArgumentCaptor.forClass(Player.PlayerId.class);
        ArgumentCaptor<BigDecimal> amountCaptor = ArgumentCaptor.forClass(BigDecimal.class);
        ArgumentCaptor<Team.TeamId> winnerCaptor = ArgumentCaptor.forClass(Team.TeamId.class);
        ArgumentCaptor<Team.TeamId> creatorCaptor = ArgumentCaptor.forClass(Team.TeamId.class);

        verify(mockedProcessPlayerTransferUseCase).process(
                playerIdCaptor.capture(),
                amountCaptor.capture(),
                winnerCaptor.capture(),
                creatorCaptor.capture());

        assertThat(playerIdCaptor.getValue()).isEqualTo(Player.PlayerId.of("playerId"));
        assertThat(amountCaptor.getValue()).isEqualTo(BigDecimal.TEN);
        assertThat(winnerCaptor.getValue()).isEqualTo(Team.TeamId.of("auctionWinner"));
        assertThat(creatorCaptor.getValue()).isEqualTo(Team.TeamId.of("auctionCreator"));
    }
}
