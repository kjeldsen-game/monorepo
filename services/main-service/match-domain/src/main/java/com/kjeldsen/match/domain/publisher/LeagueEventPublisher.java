package com.kjeldsen.match.domain.publisher;

import com.kjeldsen.player.domain.events.LeagueEvent;

public interface LeagueEventPublisher {

    void publishLeagueEvent(LeagueEvent leagueEvent);
}
