package com.kjeldsen.player.application.usecases.trainings;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.generator.FallOffCliffGenerator;
import com.kjeldsen.player.domain.repositories.PlayerFallOffCliffEventWriteRepository;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
class RegisterFallOfCliffUseCaseTest {
    private final PlayerReadRepository mockedPlayerReadRepository = Mockito.mock(PlayerReadRepository.class);
    private final PlayerWriteRepository mockedPlayerWriteRepository = Mockito.mock(PlayerWriteRepository.class);
    private final PlayerFallOffCliffEventWriteRepository mockedPlayerFallOffCliffEventWriteRepository =
        Mockito.mock(PlayerFallOffCliffEventWriteRepository.class);
    private final RegisterFallOfCliffUseCase registerFallOfCliffUseCase = new RegisterFallOfCliffUseCase(
        mockedPlayerReadRepository, mockedPlayerWriteRepository, mockedPlayerFallOffCliffEventWriteRepository
    );

    @Test
    @DisplayName("Should throw an error if player age is invalid")
    public void should_throw_an_error_if_player_age_is_invalid() {
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("teamID"), 5);
        players.get(4).setAge(PlayerAge.builder().days(12.0).months(5.0).years(15).build());
        when(mockedPlayerReadRepository.findPlayerOverAge(30))
            .thenReturn(players);

        assertEquals("Invalid Player Age", assertThrows(
            IllegalArgumentException.class, registerFallOfCliffUseCase::register).getMessage());
    }

    @Test
    @DisplayName("Should set player fall off cliff")
    public void should_set_player_fall_off_cliff() {
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("teamID"), 1);
        players.get(0).getAge().setYears(34);
        when(mockedPlayerReadRepository.findPlayerOverAge(30))
            .thenReturn(players);
        registerFallOfCliffUseCase.register();
        assertTrue(players.get(0).isFallCliff());
    }

    @Test
    @DisplayName("Should not set player fall off cliff")
    public void should_not_set_player_fall_off_cliff() {
        List<Player> players = TestData.generateTestPlayers(Team.TeamId.of("teamID"), 1);
        players.get(0).getAge().setYears(32);
        when(mockedPlayerReadRepository.findPlayerOverAge(30))
            .thenReturn(players);

        MockedStatic<FallOffCliffGenerator> mockedStatic = mockStatic(FallOffCliffGenerator.class);
        mockedStatic.when(() -> FallOffCliffGenerator.checkIfFallOffCliffHappened(players.get(0).getAge().getYears()))
            .thenReturn(false);
        registerFallOfCliffUseCase.register();

        assertFalse(players.get(0).isFallCliff());
    }
}