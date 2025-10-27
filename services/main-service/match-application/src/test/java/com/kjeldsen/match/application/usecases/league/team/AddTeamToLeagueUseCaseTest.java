package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddTeamToLeagueUseCaseTest {

    private final AddTeamToPreseasonLeagueUseCase mockedAddTeamToPreseasonLeagueUseCase = Mockito.mock(AddTeamToPreseasonLeagueUseCase.class);

    private final AddTeamToActiveLeagueUseCase mockedAddTeamToActiveLeagueUseCase = Mockito.mock(AddTeamToActiveLeagueUseCase.class);
    @Mock
    private LeagueReadRepository mockedLeagueReadRepository;


    @InjectMocks
    private final AddTeamToLeagueUseCase addTeamToLeagueUseCase = new AddTeamToLeagueUseCase(
        mockedAddTeamToPreseasonLeagueUseCase, mockedAddTeamToActiveLeagueUseCase
    );


    @Test
    @DisplayName("Should add team to preseason league when no league start yet")
    void should_add_team_to_preseason_league_when_no_league_start_yet() {
        Mockito.when(mockedLeagueReadRepository.findAllByStatus(League.LeagueStatus.ACTIVE)).thenReturn(
            Collections.emptyList());

        addTeamToLeagueUseCase.add("teamId", "teamName");

        verify(mockedAddTeamToPreseasonLeagueUseCase, times(1))
            .add("teamName", "teamId");
    }

    @Test
    @DisplayName("Should add team to active season without free league spot")
    void should_add_team_to_active_season_without_free_league_sport() {
        Mockito.when(mockedLeagueReadRepository.findAllByStatus(League.LeagueStatus.ACTIVE)).thenReturn(
            List.of(
                League.builder().status(League.LeagueStatus.ACTIVE).id(League.LeagueId.of("leagueId"))
                    .teams(Map.of("team123", new League.LeagueStats())).build()
            ));

            addTeamToLeagueUseCase.add("teamId", "teamName");
            verify(mockedAddTeamToActiveLeagueUseCase, times(1))
            .addWithoutSpot("teamName", "teamId");
    }

    @Test
    @DisplayName("Should add team to active season with free league spot")
    void should_add_team_to_active_season_with_free_league_sport() {
        List<League> testLeagues =  List.of(
            League.builder().status(League.LeagueStatus.ACTIVE).id(League.LeagueId.of("leagueId"))
                .teams(Map.of("team123", new League.LeagueStats())).build()
        );
        testLeagues.get(0).getTeams().get("team123").setBot(true);
        Mockito.when(mockedLeagueReadRepository.findAllByStatus(League.LeagueStatus.ACTIVE)).thenReturn(testLeagues);



        addTeamToLeagueUseCase.add("teamId", "teamName");
        verify(mockedAddTeamToActiveLeagueUseCase, times(1))
            .addWithSpot(testLeagues,"teamName", "teamId");
    }
}