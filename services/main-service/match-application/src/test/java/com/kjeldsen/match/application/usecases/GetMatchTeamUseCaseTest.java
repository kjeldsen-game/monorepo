package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetMatchTeamUseCaseTest {

    private final GetMatchUseCase mockedGetMatchUseCase = Mockito.mock(GetMatchUseCase.class);
    private final GetMatchTeamUseCase getMatchTeamUseCase = new GetMatchTeamUseCase(mockedGetMatchUseCase);

    @Test
    @DisplayName("Should throw error that Team not found")
    void should_throw_error_that_team_not_found() {
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(Match.builder()
            .home(Team.builder().id("home").build())
            .away(Team.builder().id("away").build()).build());

        assertEquals("Team not found!", assertThrows(RuntimeException.class, () -> {
            getMatchTeamUseCase.getMatchAndTeam("matchId", "teamId");
        }).getMessage());

        verify(mockedGetMatchUseCase).get("matchId");
    }

    @Test
    @DisplayName("Should return match team and role")
    void should_return_team() {
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(Match.builder()
            .id("id")
            .home(Team.builder().id("home").build())
            .away(Team.builder().id("away").build()).build());

        GetMatchTeamUseCase.MatchAndTeam matchAndTeam = getMatchTeamUseCase.getMatchAndTeam("matchId", "home");
        assertEquals("home", matchAndTeam.team().getId());
        verify(mockedGetMatchUseCase).get("matchId");
    }
}