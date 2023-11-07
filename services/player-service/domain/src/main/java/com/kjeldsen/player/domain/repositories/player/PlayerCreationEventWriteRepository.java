package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.events.PlayerCreationEvent;

public interface PlayerCreationEventWriteRepository {
    PlayerCreationEvent save(PlayerCreationEvent playerCreationEvent);
}
