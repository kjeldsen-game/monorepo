package com.kjeldsen.match.domain.publisher;

import com.kjeldsen.player.domain.events.MatchEvent;

public interface MatchEventPublisher {

    void publishMatchEvent(MatchEvent matchEvent);
}
