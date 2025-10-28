package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RescheduleLeagueUseCaseTest {

    private final MatchReadRepository mockedMatchReadRepository = Mockito.mock(MatchReadRepository.class);
    private final MatchWriteRepository mockedMatchWriteRepository = Mockito.mock(MatchWriteRepository.class);
    private final RescheduleLeagueUseCase rescheduleLeagueUseCase = new RescheduleLeagueUseCase(
        mockedMatchReadRepository, mockedMatchWriteRepository);

    @Test
    @DisplayName("Should reschedule matches of old team to new team")
    void should_reschedule_old_team_to_new_team() {
        List<Match> matches =  List.of(Match.builder().home(Team.builder().id("bot").build())
            .away(Team.builder().id("team").build()).build(), Match.builder().home(Team.builder().id("bot").build())
            .away(Team.builder().id("team34").build()).build());

        Mockito.when(mockedMatchReadRepository.findMatchesByLeagueIdAndTeamIdAndStatus(
            "bot", "league", Match.Status.SCHEDULED
        )).thenReturn(matches);

        rescheduleLeagueUseCase.rescheduleLeague("league", "bot",
            "newTeamId", "testNAme");

        assertThat(matches.stream().filter(match -> match.getAway().getId().equals("bot") ||
            match.getHome().getId().equals("bot"))).hasSize(0);
        assertThat(matches.stream().filter(match -> match.getAway().getId().equals("newTeamId") ||
            match.getHome().getId().equals("newTeamId"))).hasSize(2);
    }
}