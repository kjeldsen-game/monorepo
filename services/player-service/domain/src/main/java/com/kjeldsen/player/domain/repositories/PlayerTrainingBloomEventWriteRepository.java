package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerBloomEvent;

public interface PlayerTrainingBloomEventWriteRepository {
    PlayerBloomEvent save(PlayerBloomEvent playerBloomEvent);
}
