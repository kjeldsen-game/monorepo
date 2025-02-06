package com.kjeldsen.match.application.usecases.league;

import com.kjeldsen.match.domain.entities.League;
import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetLeagueUseCaseTest {

    private final LeagueReadRepository mockedLeagueReadRepository = Mockito.mock(LeagueReadRepository.class);
    private final GetLeagueUseCase getLeagueUseCase = new GetLeagueUseCase(mockedLeagueReadRepository);

    @Test
    @DisplayName("Should throw error when league not found")
    void should_throw_error_when_league_not_found() {

        when(mockedLeagueReadRepository.findById(League.LeagueId.of("leagueId"))).thenReturn(Optional.empty());

        assertEquals("League not found", assertThrows(RuntimeException.class, () -> {
            getLeagueUseCase.get("leagueId");}).getMessage());

        verify(mockedLeagueReadRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Should return league")
    void should_return_league() {
        when(mockedLeagueReadRepository.findById(League.LeagueId.of("leagueId")))
            .thenReturn(Optional.of(League.builder().id(League.LeagueId.of("leagueId")).build()));
        League league = getLeagueUseCase.get("leagueId");
        assertNotNull(league);
        assertEquals("leagueId", league.getId().value());
    }
}