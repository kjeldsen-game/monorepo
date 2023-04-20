package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerCreationEvent;

public interface PlayerCreationEventWriteRepository {
    PlayerCreationEvent save(PlayerCreationEvent playerCreationEvent);
}
