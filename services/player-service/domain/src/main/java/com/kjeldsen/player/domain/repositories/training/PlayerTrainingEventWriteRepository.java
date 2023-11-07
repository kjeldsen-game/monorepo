package com.kjeldsen.player.domain.repositories.training;

import com.kjeldsen.player.domain.events.PlayerTrainingEvent;

public interface PlayerTrainingEventWriteRepository {
    PlayerTrainingEvent save(PlayerTrainingEvent playerTrainingEvent);
}
