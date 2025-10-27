package com.kjeldsen.match.listener;

import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.match.application.usecases.league.team.AddBotTeamsToLeagueUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class TeamCreationEventListenerTest {

    @Mock
    private AddBotTeamsToLeagueUseCase mockedAddBotTeamsToLeagueUseCase;
    @InjectMocks
    private TeamCreationEventListener teamCreationEventListener;

    @Test
    @DisplayName("Should handle event and process")
    void should_handle_event_and_process() {
        Map<String, String> teams = Map.of(
            "team1", "teamName",
            "team2", "teamName2"
        );

        var event = TeamCreationEvent.builder().teams(teams).leagueId("leagueId").isBots(true).build();
        teamCreationEventListener.handleTeamCreationEvent(event);
        Mockito.verify(mockedAddBotTeamsToLeagueUseCase, Mockito.times(1))
            .add(teams, "leagueId");
    }
}