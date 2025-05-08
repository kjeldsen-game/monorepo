package com.kjeldsen.match.application.usecases;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.lib.model.team.TeamModifiersClient;
import com.kjeldsen.match.application.usecases.common.BaseClientTest;
import com.kjeldsen.match.application.usecases.league.UpdateLeagueStandingsUseCase;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.match.domain.clients.models.team.TeamModifiers;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.exceptions.InvalidMatchStatusException;
import com.kjeldsen.match.domain.publisher.MatchEventPublisher;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExecuteMatchUseCaseTest extends BaseClientTest {

    private final GetMatchAttendanceUseCase mockedGetMatchAttendanceUseCase = mock(GetMatchAttendanceUseCase.class);
    private final MatchWriteRepository mockedMatchWriteRepository = mock(MatchWriteRepository.class);
    private final MatchEventPublisher mockedMatchEventPublisher = mock(MatchEventPublisher.class);
    private final GetMatchUseCase mockedGetMatchUseCase = mock(GetMatchUseCase.class);
    private final UpdateLeagueStandingsUseCase mockedUpdateLeagueStandingsUseCase = Mockito.mock(UpdateLeagueStandingsUseCase.class);
    private final TeamClientApi mockedTeamClient = Mockito.mock(TeamClientApi.class);
    private final ExecuteMatchUseCase executeMatchUseCase = new ExecuteMatchUseCase(mockedGetMatchAttendanceUseCase,
        mockedMatchWriteRepository, mockedMatchEventPublisher, mockedGetMatchUseCase, mockedUpdateLeagueStandingsUseCase, mockedTeamClient);

    @Test
    @DisplayName("Should throw error when match status is invalid")
    void should_throw_error_when_match_status_is_invalid() {
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(Match.builder().id("matchId").status(Match.Status.PLAYED).build());
        assertEquals("Invalid match status!", assertThrows(InvalidMatchStatusException.class, () -> {
            executeMatchUseCase.execute("matchId");
        }).getMessage());
    }


    @Test
    @DisplayName("Should execute Match successfully and publish event for league")
    void shouldExecuteMatchSuccessfully() {
        Match match = mock(Match.class);
        Team homeTeam = mock(Team.class);
        Team awayTeam = mock(Team.class);

        when(match.getId()).thenReturn("match-id");
        when(match.getHome()).thenReturn(homeTeam);
        when(match.getAway()).thenReturn(awayTeam);
        when(match.getStatus()).thenReturn(Match.Status.SCHEDULED);
        when(match.getLeagueId()).thenReturn("league-id");
        when(homeTeam.getId()).thenReturn("home-team-id");
        when(awayTeam.getId()).thenReturn("away-team-id");
        when(mockedGetMatchUseCase.get(any())).thenReturn(match);

        GameState mockGameState = mock(GameState.class);

        TeamState mockHomeTeam = mock(TeamState.class);
        TeamState mockAwayTeam = mock(TeamState.class);

        when(mockGameState.getHome()).thenReturn(mockHomeTeam);
        when(mockGameState.getAway()).thenReturn(mockAwayTeam);

        when(mockHomeTeam.getScore()).thenReturn(1);
        when(mockAwayTeam.getScore()).thenReturn(2);

        MatchReport testReport = MatchReport.builder()
            .awayAttendance(800)
            .homeAttendance(1000)
            .homeScore(1)
            .awayScore(2)
            .build();

        when(match.getMatchReport()).thenReturn(testReport);
        when(mockedTeamClient.getTeam("home-team-id", null, null)).thenReturn(
            List.of(TeamClient.builder().players(Collections.EMPTY_LIST).teamModifiers(TeamModifiersClient.builder().horizontalPressure("SWARM_CENTRE")
                .verticalPressure("MID_PRESSURE").tactic("DOUBLE_TEAM").build()).build()));

        when(mockedTeamClient.getTeam("away-team-id", null, null)).thenReturn(
            List.of(TeamClient.builder().players(Collections.EMPTY_LIST).teamModifiers(TeamModifiersClient.builder().horizontalPressure("SWARM_CENTRE")
                .verticalPressure("MID_PRESSURE").tactic("DOUBLE_TEAM").build()).build()));

        try (MockedStatic<Game> mockedGame = mockStatic(Game.class)) {
            mockedGame.when(() -> Game.play(any(Match.class))).thenReturn(mockGameState);

            Map<String, Integer> attendance = new HashMap<>();
            attendance.put("homeAttendance", 1000);
            attendance.put("awayAttendance", 800);

            assertDoesNotThrow(() -> executeMatchUseCase.execute("match-id"));
            mockedGame.verify(() -> Game.play(any(Match.class)), times(1));
        }

        verify(mockedMatchWriteRepository, times(1)).save(any(Match.class));
        verify(mockedMatchEventPublisher, times(1)).publishMatchEvent(any());
    }
}