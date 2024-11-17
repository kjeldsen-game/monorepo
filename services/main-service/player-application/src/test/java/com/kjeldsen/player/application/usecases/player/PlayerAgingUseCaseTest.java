package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;

class PlayerAgingUseCaseTest {

    private final PlayerReadRepository playerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository playerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerAgingUseCase playerAgingUseCase = new PlayerAgingUseCase(playerReadRepository,
        playerWriteRepository);

    @Test
    @DisplayName("Should increase players age")
    public void should_increase_players_age() {

        List<Player> players = TestData.generateTestPlayers(Team.TeamId.generate(),10);
        when(playerReadRepository.findAll()).thenReturn(players);
        playerAgingUseCase.increaseAge();
        verify(playerReadRepository, times(1)).findAll();
        verify(playerWriteRepository, times(10)).save(any(Player.class));
    }
}