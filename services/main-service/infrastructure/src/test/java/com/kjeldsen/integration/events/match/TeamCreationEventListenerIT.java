package com.kjeldsen.integration.events.match;

import com.kjeldsen.integration.events.AbstractEventIT;
import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.match.application.usecases.league.team.AddBotTeamsToLeagueUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

public class TeamCreationEventListenerIT extends AbstractEventIT {

    @MockitoBean
    private AddBotTeamsToLeagueUseCase addBotTeamsToLeagueUseCase;

    @Test
    @DisplayName("Should handle the TeamCreationEvent and verify function call")
    void should_handle_team_creation_event_and_verify_function_call() {
        Map<String, String> teams = Map.of(
            "team1", "teamName",
            "team2", "teamName2"
        );
        testEventPublisher.publishEvent(TeamCreationEvent.builder().leagueId("leagueId")
            .teams(teams)
            .isBots(true)
            .build());

        Mockito.verify(addBotTeamsToLeagueUseCase,
            Mockito.times(1)).add(teams, "leagueId");
    }

    @Test
    @DisplayName("Should handle the TeamCreationEvent but do nothing")
    void should_handle_team_creation_event_and_do_nothing() {
        Map<String, String> teams = Map.of(
            "team1", "teamName",
            "team2", "teamName2"
        );
        testEventPublisher.publishEvent(TeamCreationEvent.builder().leagueId("leagueId")
            .teams(teams)
            .build());

        Mockito.verify(addBotTeamsToLeagueUseCase, Mockito.times(0))
            .add(teams, "leagueId");
    }


}
