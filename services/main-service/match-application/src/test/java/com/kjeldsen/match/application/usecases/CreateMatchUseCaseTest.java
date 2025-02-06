package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.usecases.common.BaseClientTest;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateMatchUseCaseTest extends BaseClientTest {

    private final MatchWriteRepository mockedMatchWriteRepository = Mockito.mock(MatchWriteRepository.class);
    private final TeamClientMatch mockedTeamClient = Mockito.mock(TeamClientMatch.class);
    private final CreateMatchUseCase createMatchUseCase = new CreateMatchUseCase(mockedMatchWriteRepository,
        mockedTeamClient);

    @Test
    @DisplayName("Should create match with the PENDING status due no league")
    void should_create_match_without_league() {
        when(mockedTeamClient.getTeam("home", "token")).thenReturn(
            TeamDTO.builder().id("home").build());
        when(mockedTeamClient.getTeam("away", "token")).thenReturn(
            TeamDTO.builder().id("away").build());

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
        when(mockedTeamClient.getTeam("home", "token")).thenReturn(
            TeamDTO.builder().id("home").build());
        when(mockedTeamClient.getTeam("away", "token")).thenReturn(
            TeamDTO.builder().id("away").build());

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