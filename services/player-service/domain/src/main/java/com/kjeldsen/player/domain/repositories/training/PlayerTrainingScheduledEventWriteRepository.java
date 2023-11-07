package com.kjeldsen.player.domain.repositories.training;

import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;

public interface PlayerTrainingScheduledEventWriteRepository {
    PlayerTrainingScheduledEvent save(PlayerTrainingScheduledEvent playerTrainingScheduledEvent);
}
