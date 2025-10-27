package com.kjeldsen.player.listener;

import com.kjeldsen.lib.events.LeagueStartBotTeamsCreationEvent;
import com.kjeldsen.player.application.usecases.team.CreateBotTeamsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LeagueStartBotTeamsCreationEventListener {

    private final CreateBotTeamsUseCase createBotTeamUseCase;

    @EventListener
    public void handleAuctionEndEvent(LeagueStartBotTeamsCreationEvent leagueStartBotTeamsCreationEvent) {
        log.debug("LeagueStartBotTeamsCreationEvent received: {}", leagueStartBotTeamsCreationEvent);
        createBotTeamUseCase.create(leagueStartBotTeamsCreationEvent.getCount(), leagueStartBotTeamsCreationEvent.getLeagueId());
    }
}
