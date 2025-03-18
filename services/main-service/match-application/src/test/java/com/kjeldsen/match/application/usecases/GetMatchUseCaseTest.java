package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.exceptions.MatchNotFoundException;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetMatchUseCaseTest {

    private final MatchReadRepository mockedMatchReadRepository = Mockito.mock(MatchReadRepository.class);
    private final GetMatchUseCase getMatchUseCase = new GetMatchUseCase(mockedMatchReadRepository);

    @Test
    @DisplayName("Should throw error when match not found")
    void should_throw_error_when_match_not_found() {
        when(mockedMatchReadRepository.findOneById("id")).thenReturn(Optional.empty());

        assertEquals("Match not found!", assertThrows(MatchNotFoundException.class, () -> {
            getMatchUseCase.get("id");
        }).getMessage());

        verify(mockedMatchReadRepository).findOneById("id");
        verifyNoMoreInteractions(mockedMatchReadRepository);
    }

    @Test
    @DisplayName("Should return matches based on the leagueId")
    void should_return_matches_based_on_leagueId() {
        when(mockedMatchReadRepository.findMatchesByLeagueId("id")).thenReturn(List.of(
            Match.builder().leagueId("id").build()));

        List<Match> matches = getMatchUseCase.getAll(null, "id");
        assertThat(matches).isNotEmpty();
        verify(mockedMatchReadRepository).findMatchesByLeagueId("id");
        verifyNoMoreInteractions(mockedMatchReadRepository);
    }

    @Test
    @DisplayName("Should return matches based on the teamId")
    void should_return_matches_based_on_teamId() {
        when(mockedMatchReadRepository.findMatchesByTeamId("id")).thenReturn(List.of(
            Match.builder().leagueId("id").home(Team.builder().id("id").build()).build()));

        List<Match> matches = getMatchUseCase.getAll("id", null);
        assertThat(matches).isNotEmpty();
        verify(mockedMatchReadRepository).findMatchesByTeamId("id");
        verifyNoMoreInteractions(mockedMatchReadRepository);
    }
}