package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.Team;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateMatchChallengeUseCaseTest {

    private final GetMatchUseCase mockedGetMatchUseCase = Mockito.mock(GetMatchUseCase.class);
    private final MatchWriteRepository mockedMatchWriteRepository = Mockito.mock(MatchWriteRepository.class);
    private final UpdateMatchChallengeUseCase updateMatchChallengeUseCase = new UpdateMatchChallengeUseCase(
        mockedGetMatchUseCase, mockedMatchWriteRepository);

    @Test
    @DisplayName("Should throw error if invalid status")
    void should_throw_error_if_invalid_status() {
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(Match.builder().status(Match.Status.ACCEPTED)
            .home(Team.builder().id("home").build())
            .away(Team.builder().id("away").build()).build());
        assertEquals("Match is not in the PENDING status!", assertThrows(RuntimeException.class, () -> {
            updateMatchChallengeUseCase.update("matchId", Match.Status.ACCEPTED);
        }).getMessage());
    }

    @Test
    @DisplayName("Should not throw error if invalid status")
    void should_throw_error_if_invalid_status_but_self_challenge() {
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(Match.builder()
            .home(Team.builder().id("home").build())
            .away(Team.builder().id("home").build())
            .status(Match.Status.ACCEPTED).build());

        assertDoesNotThrow(() -> updateMatchChallengeUseCase.update("matchId", Match.Status.REJECTED));
    }

    @Test
    @DisplayName("Should update the match status to ACCEPTED")
    void should_update_the_status_if_status_is_ACCEPTED() {
        Match match = Match.builder().status(Match.Status.PENDING)
            .home(Team.builder().id("home").build())
            .away(Team.builder().id("away").build()).build();
        when(mockedGetMatchUseCase.get("matchId")).thenReturn(match);
        updateMatchChallengeUseCase.update("matchId", Match.Status.ACCEPTED);
        assertEquals(Match.Status.ACCEPTED, match.getStatus());

        verify(mockedMatchWriteRepository, Mockito.times(1)).save(match);
    }
}