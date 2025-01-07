package com.kjeldsen.league.listener;

import com.kjeldsen.league.application.usecases.UpdateLeagueStandingsUseCase;
import com.kjeldsen.player.domain.events.MatchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MatchEventListenerLeague {

    private final UpdateLeagueStandingsUseCase updateLeagueStandingsUseCase;

    @EventListener
    public void handleMatchEvent(MatchEvent matchEvent) {
        log.info("MatchEvent received: {}", matchEvent);
        updateLeagueStandingsUseCase.update(matchEvent.getLeagueId(),
            matchEvent.getHomeTeamId(), matchEvent.getAwayTeamId(), matchEvent.getHomeScore(), matchEvent.getAwayScore());
    }
}
