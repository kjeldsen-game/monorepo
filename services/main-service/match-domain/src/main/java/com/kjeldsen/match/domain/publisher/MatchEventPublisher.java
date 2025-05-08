package com.kjeldsen.match.domain.publisher;

import com.kjeldsen.lib.events.MatchEvent;

public interface MatchEventPublisher {

    void publishMatchEvent(MatchEvent matchEvent);
}
