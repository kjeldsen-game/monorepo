package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateLeagueStandingsUseCaseTest {

    private final LeagueWriteRepository mockedLeagueWriteRepository = Mockito.mock(LeagueWriteRepository.class);
    private final GetLeagueUseCase mockedGetLeagueUseCase = Mockito.mock(GetLeagueUseCase.class);
    private final UpdateLeagueStandingsUseCase updateLeagueStandingsUseCase = new UpdateLeagueStandingsUseCase(
        mockedLeagueWriteRepository, mockedGetLeagueUseCase);


    @Test
    @DisplayName("Should update the league standing")
    void should_update_the_league_standing() {
        League league = League.builder().id(League.LeagueId.of("leagueId")).teams(
            new HashMap<>()
        ).build();
        league.getTeams().put("exampleTeam", new League.LeagueStats());
        league.getTeams().put("exampleTeam2", new League.LeagueStats());
        when(mockedGetLeagueUseCase.get("leagueId")).thenReturn(league);
        updateLeagueStandingsUseCase.update("leagueId", "exampleTeam", "exampleTeam2", 1, 2);


        assertEquals(1, league.getTeams().get("exampleTeam").getGoalsScored());
        assertEquals(2, league.getTeams().get("exampleTeam").getGoalsReceived());
        assertEquals(1, league.getTeams().get("exampleTeam2").getPosition());
        assertEquals(2, league.getTeams().get("exampleTeam").getPosition());


        verify(mockedGetLeagueUseCase, times(1)).get("leagueId");
        verify(mockedLeagueWriteRepository,times(1)).save(league);
    }

}