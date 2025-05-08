package com.kjeldsen.player.domain.publishers;

import com.kjeldsen.lib.events.TeamCreationEvent;

public interface TeamCreationEventPublisher {
    void publish(TeamCreationEvent teamCreationEvent);
}
