package com.kjeldsen.match.domain.publisher;

import com.kjeldsen.lib.events.LeagueEvent;

public interface LeagueEventPublisher {

    void publishLeagueEvent(LeagueEvent leagueEvent);
}
