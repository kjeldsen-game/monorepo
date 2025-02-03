package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddTeamToLeagueUseCaseTest {

    private final LeagueWriteRepository mockedLeagueWriteRepository = Mockito.mock(LeagueWriteRepository.class);
    private final LeagueReadRepository mockedLeagueReadRepository  = Mockito.mock(LeagueReadRepository.class);
    // TODO add publisher here and listener in the Team
    private final TeamReadRepository mockedTeamReadRepository = Mockito.mock(TeamReadRepository.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final AddTeamToLeagueUseCase addTeamToLeagueUseCase = new AddTeamToLeagueUseCase(
        mockedLeagueWriteRepository, mockedLeagueReadRepository, mockedTeamReadRepository, mockedTeamWriteRepository);

    @Test
    @DisplayName("Should throw error when team not found")
    void should_throw_error_when_team_not_found() {
        when(mockedTeamReadRepository.findById(Team.TeamId.of("teamId"))).thenReturn(Optional.empty());

        assertEquals("Team not found", assertThrows(RuntimeException.class, () -> {
            addTeamToLeagueUseCase.add("teamId", BigDecimal.ONE);}).getMessage());

        verify(mockedTeamReadRepository, times(1)).findById(any(Team.TeamId.class));
    }

    @Test
    @DisplayName("Should create a league if all leagues are full")
    void should_create_league_if_all_leagues_are_full() {
        when(mockedTeamReadRepository.findById(Team.TeamId.of("teamId"))).thenReturn(Optional.of(
            Team.builder().id(Team.TeamId.of("teamId")).name("teamName").build()));
        when(mockedLeagueReadRepository.findAll()).thenReturn(Collections.emptyList());
        when(mockedLeagueWriteRepository.save(any(League.class))).thenReturn(
            League.builder().id(League.LeagueId.of("leagueId")).build());
        addTeamToLeagueUseCase.add("teamId", BigDecimal.ONE);

        verify(mockedLeagueReadRepository, times(1)).findAll();
        verify(mockedLeagueWriteRepository,times(1)).save(any(League.class));
        verify(mockedTeamReadRepository,times(1)).findById(any());
        verify(mockedTeamWriteRepository, times(1)).save(any(Team.class));
    }

    @Test
    @DisplayName("Should assign team to existing league")
    void should_assign_team_to_existing_league() {
        League league = League.builder().id(League.LeagueId.of("leagueId")).teams(
            new HashMap<>()
        ).build();
        league.getTeams().put("exampleTeam", new League.LeagueStats());
        Team team =  Team.builder().id(Team.TeamId.of("teamId")).name("teamName").build();
        when(mockedTeamReadRepository.findById(Team.TeamId.of("teamId"))).thenReturn(Optional.of(team));
        when(mockedLeagueReadRepository.findAll()).thenReturn(List.of(league));
        when(mockedLeagueWriteRepository.save(any(League.class))).thenReturn(league);

        addTeamToLeagueUseCase.add("teamId", BigDecimal.ONE);

        assertEquals("leagueId", team.getLeagueId());
        assertEquals(2, league.getTeams().size());
        verify(mockedLeagueReadRepository, times(1)).findAll();
        verify(mockedLeagueWriteRepository,times(1)).save(any(League.class));
        verify(mockedTeamReadRepository,times(1)).findById(any());
        verify(mockedTeamWriteRepository, times(1)).save(any(Team.class));
    }
}