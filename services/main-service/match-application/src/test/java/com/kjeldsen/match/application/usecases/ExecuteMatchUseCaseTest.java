package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.usecases.league.UpdateLeagueStandingsUseCase;
import com.kjeldsen.match.domain.Game;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.modifers.HorizontalPressure;
import com.kjeldsen.match.domain.modifers.Tactic;
import com.kjeldsen.match.domain.modifers.VerticalPressure;
import com.kjeldsen.match.domain.publisher.MatchEventPublisher;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.match.domain.state.GameState;
import com.kjeldsen.match.domain.state.TeamState;
import com.kjeldsen.player.domain.TeamModifiers;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExecuteMatchUseCaseTest {

    private final GetMatchAttendanceUseCase mockedGetMatchAttendanceUseCase = mock(GetMatchAttendanceUseCase.class);
    private final MatchWriteRepository mockedMatchWriteRepository = mock(MatchWriteRepository.class);
    private final TeamReadRepository mockedTeamReadRepository = mock(TeamReadRepository.class);
    private final PlayerReadRepository mockedPlayerReadRepository = mock(PlayerReadRepository.class);
    private final MatchEventPublisher mockedMatchEventPublisher = mock(MatchEventPublisher.class);
    private final GetMatchUseCase mockedGetMatchUseCase = mock(GetMatchUseCase.class);
    private final UpdateLeagueStandingsUseCase mockedUpdateLeagueStandingsUseCase = Mockito.mock(UpdateLeagueStandingsUseCase.class);
    private final ExecuteMatchUseCase executeMatchUseCase = new ExecuteMatchUseCase(mockedGetMatchAttendanceUseCase,
        mockedTeamReadRepository, mockedMatchWriteRepository, mockedPlayerReadRepository, mockedMatchEventPublisher,
        mockedGetMatchUseCase, mockedUpdateLeagueStandingsUseCase);

    @Test
    @DisplayName("Should throw error when match status is invalid")
    void should_throw_error_when_match_status_is_invalid() {
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(Match.builder().id("matchId").status(Match.Status.PLAYED).build());
        assertEquals("Invalid match status", assertThrows(RuntimeException.class, () -> {
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

        try (MockedStatic<Game> mockedGame = mockStatic(Game.class)) {
            mockedGame.when(() -> Game.play(any(Match.class))).thenReturn(mockGameState);

            Map<String, Integer> attendance = new HashMap<>();
            attendance.put("homeAttendance", 1000);
            attendance.put("awayAttendance", 800);

            when(mockedGetMatchAttendanceUseCase.get(any(), any())).thenReturn(attendance);
            com.kjeldsen.player.domain.TeamModifiers modifiers = new TeamModifiers(
                TeamModifiers.Tactic.TIKA_TAKA, TeamModifiers.VerticalPressure.LOW_PRESSURE, TeamModifiers.HorizontalPressure.NO_HORIZONTAL_FOCUS);

            when(mockedTeamReadRepository.findById(com.kjeldsen.player.domain.Team.TeamId.of("home-team-id"))).thenReturn(
                Optional.of(com.kjeldsen.player.domain.Team.builder().teamModifiers(modifiers).build()));

            when(mockedTeamReadRepository.findById(com.kjeldsen.player.domain.Team.TeamId.of("away-team-id"))).thenReturn(
                Optional.of(com.kjeldsen.player.domain.Team.builder().teamModifiers(modifiers).build()));

            assertDoesNotThrow(() -> executeMatchUseCase.execute("match-id"));
            mockedGame.verify(() -> Game.play(any(Match.class)), times(1));
        }

        verify(mockedMatchWriteRepository, times(1)).save(any(Match.class));
        verify(mockedMatchEventPublisher, times(1)).publishMatchEvent(any());
    }
}