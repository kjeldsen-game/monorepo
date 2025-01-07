package com.kjeldsen.league.listener;

import com.kjeldsen.league.application.usecases.AddTeamToLeagueUseCase;
import com.kjeldsen.player.domain.events.TeamCreationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TeamCreationEventListener {

    private final AddTeamToLeagueUseCase addTeamToLeagueUseCase;

    @EventListener
    public void handleTeamCreationEvent(TeamCreationEvent teamCreationEvent) {
        log.info("TeamCreationEventListener received: {}", teamCreationEvent);
        addTeamToLeagueUseCase.add(teamCreationEvent.getTeamId(), teamCreationEvent.getTeamValue() );
    }
}
