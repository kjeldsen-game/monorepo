package com.kjeldsen.match.listener;

import com.kjeldsen.lib.events.TeamCreationEvent;
import com.kjeldsen.match.application.usecases.league.team.AddBotTeamsToLeagueUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamCreationEventListener {

    private final AddBotTeamsToLeagueUseCase addTeamToLeagueUseCase;

    @EventListener
    public void handleTeamCreationEvent(TeamCreationEvent teamCreationEvent) {
        log.info("TeamCreationEventListener received: {}", teamCreationEvent);
        if (teamCreationEvent.isBots()) {
            addTeamToLeagueUseCase.add(teamCreationEvent.getTeams(), teamCreationEvent.getLeagueId() );
        }
    }
}

