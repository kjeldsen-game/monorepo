package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class GetTeamUseCaseTest {

    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final GetTeamUseCase getTeamUseCase = new GetTeamUseCase(mockedTeamReadRepository);

    @Test
    public void should_throw_exception_when_team_does_not_exist() {
        String userId = "exampleUserId";
        when(mockedTeamReadRepository.findOneByUserId(userId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            getTeamUseCase.get(userId);
        });
        assertEquals(String.format("Team not found for user with ID %s", userId), exception.getMessage());
        verify(mockedTeamReadRepository).findOneByUserId(userId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    public void should_throw_exception_when_team_query_fails() {
        String userId = "exampleUserId";
        String errorMessage = "Error message";
        when(mockedTeamReadRepository.findOneByUserId(userId)).thenThrow(new RuntimeException(errorMessage));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            getTeamUseCase.get(userId);
        });
        assertEquals(errorMessage, exception.getMessage());
        verify(mockedTeamReadRepository).findOneByUserId(userId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }

    @Test
    public void should_return_team_when_found() {
        String userId = "exampleUserId";
        List<Player> examplePlayers = new ArrayList<>();
        Team teamExpected = Team.builder()
            .id(Team.TeamId.of("exampleId"))
            .name("exampleName")
            .userId("exampleUserId")
            .players(examplePlayers)
            .canteraScore(0)
            .build();
        when(mockedTeamReadRepository.findOneByUserId(userId)).thenReturn(Optional.of(teamExpected));

        Team teamResult = getTeamUseCase.get(userId);

        Assertions.assertNotNull(teamResult);
        assertEquals(teamExpected, teamResult);
        verify(mockedTeamReadRepository).findOneByUserId(userId);
        verifyNoMoreInteractions(mockedTeamReadRepository);
    }
}
