package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddTeamToPreseasonLeagueUseCaseTest {

    @Mock
    private LeagueWriteRepository mockedLeagueWriteRepository;
    @Mock
    private LeagueReadRepository mockedLeagueReadRepository;
    @InjectMocks
    private AddTeamToPreseasonLeagueUseCase addTeamToPreseasonLeagueUseCase = new AddTeamToPreseasonLeagueUseCase();

    @Test
    @DisplayName("Should create new preseason league")
    void should_create_new_preseason_league() {
        when(mockedLeagueReadRepository.findAllByStatus(League.LeagueStatus.PRESEASON)).thenReturn(Collections.emptyList());
        String leagueId = addTeamToPreseasonLeagueUseCase.add("teamName", "teamId");
        assertThat(leagueId).isNotBlank();
        verify(mockedLeagueWriteRepository,times( 1)).save(any(League.class));
    }

    @Test
    @DisplayName("Should use a existing preseason league")
    void should_use_existing_preseason_league() {
        Map<String, League.LeagueStats> teams = new HashMap<>();
        teams.put("team1", new League.LeagueStats());
        List<League> leagues = List.of(
            League.builder().id(League.LeagueId.of("leagueId232")).status(League.LeagueStatus.PRESEASON)
                .teams(teams).build()
        );
        when(mockedLeagueReadRepository.findAllByStatus(League.LeagueStatus.PRESEASON)).thenReturn(
            leagues);

        String leagueId = addTeamToPreseasonLeagueUseCase.add("teamName", "teamId");
        assertThat(leagueId).isNotBlank();
        assertThat(leagueId).isEqualTo("leagueId232");
        verify(mockedLeagueWriteRepository,times( 1)).save(any(League.class));
    }
}