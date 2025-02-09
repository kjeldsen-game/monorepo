package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.domain.entities.League;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.when;

class GenerateMatchScheduleUseCaseTest {

    private final GetLeagueUseCase mockedGetLeagueUseCase = Mockito.mock(GetLeagueUseCase.class);
    private final GenerateMatchScheduleUseCase generateMatchScheduleUseCase = new GenerateMatchScheduleUseCase(mockedGetLeagueUseCase);

    @Test
    @DisplayName("Should return list of scheduled matches")
    void should_return_list_of_scheduled_matches() {
        League league = League.builder().id(League.LeagueId.of("leagueId")).teams(
            new HashMap<>()
        ).build();
        league.getTeams().put("exampleTeam", new League.LeagueStats());
        league.getTeams().put("exampleTeam2", new League.LeagueStats());
        league.getTeams().put("exampleTeam3", new League.LeagueStats());
        league.getTeams().put("exampleTeam4", new League.LeagueStats());
        when(mockedGetLeagueUseCase.get("leagueId")).thenReturn(league);

        List<GenerateMatchScheduleUseCase. ScheduledMatch> matches =  generateMatchScheduleUseCase.generate("leagueId");


        // Assert total matches
        Assertions.assertEquals(league.getTeams().size() * (league.getTeams().size() - 1), matches.size());

        // Assert total matches for one team
        long homeOccurrences = matches.stream().filter(m -> m.homeTeamId().equals("exampleTeam")).count();
        long awayOccurrences = matches.stream().filter(m -> m.awayTeamId().equals("exampleTeam")).count();
        Assertions.assertEquals((league.getTeams().size() - 1) * 2L, homeOccurrences + awayOccurrences);

        // 4 because the 4 matches are generated in round
        Assertions.assertEquals(1L, Duration.between(matches.get(0).date(), matches.get(league.getTeams().size() / 2).date()).toDays());
    }
}