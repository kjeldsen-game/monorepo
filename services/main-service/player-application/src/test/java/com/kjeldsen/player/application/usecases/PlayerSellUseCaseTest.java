package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.events.AuctionCreationEvent;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PlayerSellUseCaseTest {
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerSellUseCase playerSellUseCase = new PlayerSellUseCase(mockedPlayerReadRepository,
        mockedPlayerWriteRepository);

    @Test
    @DisplayName("Should throw exception if player not found")
    public void should_throw_exception_if_player_not_found() {
        Player.PlayerId mockedPlayerId = Mockito.mock(Player.PlayerId.class);
        when(mockedPlayerReadRepository.findOneById(mockedPlayerId)).thenReturn(Optional.empty());

        assertEquals("Player not found", assertThrows(RuntimeException.class, () -> {
            playerSellUseCase.sell(mockedPlayerId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should throw exception if player is already on sale")
    public void should_throw_exception_if_player_is_already_on_sale() {
        Player.PlayerId mockedPlayerId = Mockito.mock(Player.PlayerId.class);
        Player mockedPlayer = Mockito.mock(Player.class);

        when(mockedPlayerReadRepository.findOneById(mockedPlayerId)).thenReturn(Optional.of(mockedPlayer));
        when(mockedPlayer.getStatus()).thenReturn(PlayerStatus.FOR_SALE);

        assertEquals("Player's status is already FOR_SALE", assertThrows(RuntimeException.class, () -> {
            playerSellUseCase.sell(mockedPlayerId);
        }).getMessage());
    }

    @Test
    @DisplayName("Should create a AuctionCreationEvent and set player's status")
    public void should_create_a_AuctionCreationEvent_and_set_player_status() {
        Player.PlayerId mockedPlayerId = Player.PlayerId.generate();
        Team.TeamId mockedTeamId = Team.TeamId.generate();
        Player mockedPlayer = TestData.generateTestPlayers(mockedTeamId, 1).get(0);
        mockedPlayer.setStatus(PlayerStatus.BENCH);

        when(mockedPlayerReadRepository.findOneById(mockedPlayerId)).thenReturn(Optional.of(mockedPlayer));

        AuctionCreationEvent testEvent = playerSellUseCase.sell(mockedPlayerId);
        assertNotNull(testEvent);
        assertEquals(PlayerStatus.FOR_SALE, mockedPlayer.getStatus());
        assertEquals(mockedPlayerId, testEvent.getPlayerId());
        assertEquals(mockedTeamId, testEvent.getTeamId());
        verify(mockedPlayerReadRepository).findOneById(mockedPlayerId);
    }
}