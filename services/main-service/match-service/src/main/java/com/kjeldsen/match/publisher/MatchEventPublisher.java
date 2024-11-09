package com.kjeldsen.match.publisher;

import com.kjeldsen.player.domain.events.MatchEvent;

public interface MatchEventPublisher {

    void publishMatchEvent(MatchEvent matchEvent);
}
