package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.match.domain.entities.ScheduledMatch;
import com.kjeldsen.match.domain.schedulers.JobQueryService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateScheduledMatchesUseCaseTest {

    private static final TriggerBotsCreationUseCase mockedTriggerBotsCreationUseCase = Mockito.mock(TriggerBotsCreationUseCase.class);
    private final JobQueryService mockedJobQueryService = Mockito.mock(JobQueryService.class);
    private final GenerateScheduledMatchesUseCase generateScheduledMatchesUseCase = new
        GenerateScheduledMatchesUseCase(mockedTriggerBotsCreationUseCase, mockedJobQueryService);

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        when(mockedTriggerBotsCreationUseCase.trigger("league1")).thenReturn(List.of("1", "2", "3", "4",
            "5", "6", "7", "8", "9", "10"));
    }

    @Test
    @DisplayName("Should schedule matches when league is not active")
    void shouldScheduleMatches() {
        List<ScheduledMatch> matches = generateScheduledMatchesUseCase.generate("league1", false);
        verify(mockedTriggerBotsCreationUseCase, times( 1)).trigger("league1");
        assertEquals(90, matches.size());
        assertEquals(18, matches.stream().filter(
            match -> match.getAwayTeamId().equals("1") || match.getHomeTeamId().equals("1")).count());
    }

    @Test
    @DisplayName("Should throw error when job is not present")
    void should_schedule_matches() {
        when(mockedJobQueryService.getNextFireTime("leagueEndJob", "league1")).thenReturn(
            Optional.empty()
        );
        assertThrows(RuntimeException.class, () -> generateScheduledMatchesUseCase.generate(
            "league1", true));
        verify(mockedTriggerBotsCreationUseCase, times( 1)).trigger("league1");
    }

    @Test
    @DisplayName("Should generate matches when league is active")
    void should_generate_matches_when_league_is_active() {
        when(mockedJobQueryService.getNextFireTime("leagueEndJob", "league")).thenReturn(
            Optional.of(ZonedDateTime.now().plusDays(12))
        );

        List<ScheduledMatch> matches = generateScheduledMatchesUseCase.generate("league1", true);
        verify(mockedTriggerBotsCreationUseCase, times( 1)).trigger("league1");
        // 12 - 1 - 1 = 10 * 5 matches in round
        assertEquals(50, matches.size());
        assertEquals(10, matches.stream().filter(
            match -> match.getAwayTeamId().equals("1") || match.getHomeTeamId().equals("1")).count());

    }
}