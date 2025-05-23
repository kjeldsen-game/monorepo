package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.events.LeagueEvent;
import com.kjeldsen.lib.model.team.TeamClient;
import com.kjeldsen.match.application.usecases.common.BaseClientTest;
import com.kjeldsen.match.domain.clients.TeamClientMatch;
import com.kjeldsen.match.domain.clients.models.team.TeamDTO;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.publisher.LeagueEventPublisher;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddTeamToLeagueUseCaseTest extends BaseClientTest {

    private final LeagueWriteRepository mockedLeagueWriteRepository = Mockito.mock(LeagueWriteRepository.class);
    private final LeagueReadRepository mockedLeagueReadRepository  = Mockito.mock(LeagueReadRepository.class);
    private final LeagueEventPublisher mockedLeagueEventPublisher = Mockito.mock(LeagueEventPublisher.class);
    private final TeamClientApi mockedTeamClient = Mockito.mock(TeamClientApi.class);
    private final AddTeamToLeagueUseCase addTeamToLeagueUseCase = new AddTeamToLeagueUseCase(
        mockedLeagueWriteRepository, mockedLeagueReadRepository, mockedLeagueEventPublisher, mockedTeamClient);


    @Test
    @DisplayName("Should create a league if all leagues are full")
    void should_create_league_if_all_leagues_are_full() {
        when(mockedTeamClient.getTeam("home",  null, null)).thenReturn(
            List.of(TeamClient.builder().id("home").name("teamName").build()));

        when(mockedLeagueReadRepository.findAll()).thenReturn(Collections.emptyList());
        when(mockedLeagueWriteRepository.save(any(League.class))).thenReturn(
            League.builder().id(League.LeagueId.of("leagueId")).build());
        addTeamToLeagueUseCase.add("home", BigDecimal.ONE);

        verify(mockedLeagueReadRepository, times(1)).findAll();
        verify(mockedLeagueWriteRepository,times(1)).save(any(League.class));
        verify(mockedTeamClient,times(1)).getTeam(any(), any(), any());
        verify(mockedLeagueEventPublisher, times(1)).publishLeagueEvent(any(LeagueEvent.class));
    }

    @Test
    @DisplayName("Should assign team to existing league")
    void should_assign_team_to_existing_league() {
        League league = League.builder().id(League.LeagueId.of("leagueId")).teams(
            new HashMap<>()
        ).build();
        league.getTeams().put("exampleTeam", new League.LeagueStats());
        TeamClient team =  TeamClient.builder().id("teamId").name("teamName").build();
        when(mockedTeamClient.getTeam("teamId", null, null)).thenReturn(List.of(team));
        when(mockedLeagueReadRepository.findAll()).thenReturn(List.of(league));
        when(mockedLeagueWriteRepository.save(any(League.class))).thenReturn(league);

        addTeamToLeagueUseCase.add("teamId", BigDecimal.ONE);

        assertEquals(2, league.getTeams().size());
        verify(mockedLeagueReadRepository, times(1)).findAll();
        verify(mockedLeagueWriteRepository,times(1)).save(any(League.class));
        verify(mockedTeamClient,times(1)).getTeam(any(), any(), any());
        verify(mockedLeagueEventPublisher, times(1)).publishLeagueEvent(any(LeagueEvent.class));
    }
}