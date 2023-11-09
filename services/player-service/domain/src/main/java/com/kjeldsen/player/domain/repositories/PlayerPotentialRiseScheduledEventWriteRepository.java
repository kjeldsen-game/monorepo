package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseScheduledEvent;

public interface PlayerPotentialRiseScheduledEventWriteRepository {
    PlayerPotentialRiseScheduledEventWriteRepository save(PlayerPotentialRiseScheduledEvent playerPotentialRiseScheduledEvent);
}
