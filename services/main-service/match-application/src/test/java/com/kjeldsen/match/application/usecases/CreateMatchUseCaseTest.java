package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateMatchUseCaseTest {

    private final MatchWriteRepository mockedMatchWriteRepository = Mockito.mock(MatchWriteRepository.class);
    // TODO in future communicate over events via modules
    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final CreateMatchUseCase createMatchUseCase = new CreateMatchUseCase(mockedMatchWriteRepository,
        mockedTeamReadRepository);

    @Test
    @DisplayName("Should throw error when home team not found")
    void should_throw_error_when_home_team_not_found() {
        when(mockedTeamReadRepository.findById(Team.TeamId.of("team"))).thenReturn(null);

        assertEquals("Home team not found", assertThrows(RuntimeException.class, () -> {
            createMatchUseCase.create("home", "away", LocalDateTime.now(), null);
        }).getMessage());

        verify(mockedTeamReadRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Should throw error when away team not found")
    void should_throw_error_when_away_team_not_found() {
        when(mockedTeamReadRepository.findById(Team.TeamId.of("home"))).thenReturn(Optional.of(Team.builder().build()));
        when(mockedTeamReadRepository.findById(Team.TeamId.of("team"))).thenReturn(null);

        assertEquals("Away team not found", assertThrows(RuntimeException.class, () -> {
            createMatchUseCase.create("home", "away", LocalDateTime.now(), null);
        }).getMessage());

        verify(mockedTeamReadRepository, times(2)).findById(any());
    }

    @Test
    @DisplayName("Should create match with the PENDING status due no league")
    void should_create_match_without_league() {
        when(mockedTeamReadRepository.findById(Team.TeamId.of("home"))).thenReturn(
            Optional.of(Team.builder().id(Team.TeamId.of("home")).build()));
        when(mockedTeamReadRepository.findById(Team.TeamId.of("away"))).thenReturn(
            Optional.of(Team.builder().id(Team.TeamId.of("away")).build()));

        createMatchUseCase.create("home", "away", LocalDateTime.now(), null);
        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        verify(mockedMatchWriteRepository).save(captor.capture());

        Match savedMatch = captor.getValue();
        assertEquals("home", savedMatch.getHome().getId());
        assertEquals("away", savedMatch.getAway().getId());
        assertEquals(Match.Status.PENDING, savedMatch.getStatus());
    }

    @Test
    @DisplayName("Should create match with the SCHEDULED status due league")
    void should_create_match_with_league() {
        when(mockedTeamReadRepository.findById(Team.TeamId.of("home"))).thenReturn(
            Optional.of(Team.builder().id(Team.TeamId.of("home")).build()));
        when(mockedTeamReadRepository.findById(Team.TeamId.of("away"))).thenReturn(
            Optional.of(Team.builder().id(Team.TeamId.of("away")).build()));

        createMatchUseCase.create("home", "away", LocalDateTime.now(), "leagueId");
        ArgumentCaptor<Match> captor = ArgumentCaptor.forClass(Match.class);
        verify(mockedMatchWriteRepository).save(captor.capture());

        Match savedMatch = captor.getValue();
        assertEquals("home", savedMatch.getHome().getId());
        assertEquals("away", savedMatch.getAway().getId());
        assertEquals("leagueId", savedMatch.getLeagueId());
        assertEquals(Match.Status.SCHEDULED, savedMatch.getStatus());
    }
}