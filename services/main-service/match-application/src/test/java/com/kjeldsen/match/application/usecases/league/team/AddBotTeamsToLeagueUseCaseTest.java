package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddBotTeamsToLeagueUseCaseTest {

    private final GetLeagueUseCase mockedGetLeagueUseCase = Mockito.mock(GetLeagueUseCase.class);
    private final LeagueWriteRepository mockedLeagueWriteRepository = Mockito.mock(LeagueWriteRepository.class);
    private final AddBotTeamsToLeagueUseCase addBotTeamsToLeagueUseCase = new AddBotTeamsToLeagueUseCase(
        mockedGetLeagueUseCase, mockedLeagueWriteRepository);

    @Test
    @DisplayName("Should add bot team to the league")
    void should_add_bot_team_to_the_league() {
        Map<String, String> teams = Map.of(
            "teamId1", "teamName1",
            "teamId2", "teamName2"
        );
        League league = League.builder()
            .id(League.LeagueId.of("leagueId"))
            .teams(new HashMap<>())
            .build();
        when(mockedGetLeagueUseCase.get(league.getId().value())).thenReturn(
            league
        );
        addBotTeamsToLeagueUseCase.add(teams, league.getId().value());
        assertThat(league.getTeams().size()).isEqualTo(2);
        verify(mockedLeagueWriteRepository,times( 1)).save(league);
    }
}