package com.kjeldsen.player.domain.publishers;

import com.kjeldsen.player.domain.events.TeamCreationEvent;

public interface TeamCreationEventPublisher {
    void publish(TeamCreationEvent teamCreationEvent);
}
