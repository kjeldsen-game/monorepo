package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerFallOffCliffEvent;

public interface PlayerFallOffCliffEventWriteRepository {
    PlayerFallOffCliffEvent save(PlayerFallOffCliffEvent playerFallOffCliffEvent);
}
