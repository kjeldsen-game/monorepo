package com.kjeldsen.match.application.usecases.league.match;

import com.kjeldsen.lib.events.LeagueStartBotTeamsCreationEvent;
import com.kjeldsen.lib.publishers.GenericEventPublisher;
import com.kjeldsen.match.application.usecases.league.GetLeagueUseCase;
import com.kjeldsen.match.domain.entities.League;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TriggerBotsCreationUseCaseTest {

    private final GetLeagueUseCase mockedGetLeagueUseCase = Mockito.mock(GetLeagueUseCase.class);
    private final GenericEventPublisher mockedEventPublisher = Mockito.mock(GenericEventPublisher.class);
    private final TriggerBotsCreationUseCase triggerBotsCreationUseCase = new TriggerBotsCreationUseCase(
        mockedGetLeagueUseCase, mockedEventPublisher);

    @Test
    @DisplayName("Should not create event to create teams")
    void should_not_create_event_to_create_teams() {
        Map<String, League.LeagueStats> teams = new HashMap<>();
        for (int i = 0; i < 9; ++i) {
            teams.put(String.valueOf(i), new League.LeagueStats());
        }
        League league = League.builder().teams(teams).build();
        when(mockedGetLeagueUseCase.get("leagueId")).thenReturn(league);
        List<String> teamsIds = triggerBotsCreationUseCase.trigger("leagueId");
        assertTrue(teamsIds.containsAll(teams.keySet()));
    }

    @Test
    @DisplayName("Should create event to create teams")
    void should_create_event_to_create_teams() {
        Map<String, League.LeagueStats> teams = new HashMap<>();
        for (int i = 0; i < 5; ++i) {
            teams.put(String.valueOf(i), new League.LeagueStats());
        }
        League league = League.builder().teams(teams).build();
        when(mockedGetLeagueUseCase.get("leagueId")).thenReturn(league);

        triggerBotsCreationUseCase.trigger("leagueId");
        verify(mockedEventPublisher, times(1))
            .publishEvent(any(LeagueStartBotTeamsCreationEvent.class));
    }
}