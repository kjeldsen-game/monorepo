package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;

public interface PlayerTrainingBloomEventWriteRepository {
    PlayerTrainingBloomEvent save(PlayerTrainingBloomEvent playerTrainingBloomEvent);
}
