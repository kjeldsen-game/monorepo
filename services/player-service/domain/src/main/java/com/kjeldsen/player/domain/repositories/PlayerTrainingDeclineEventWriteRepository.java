package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;

public interface PlayerTrainingDeclineEventWriteRepository {
    PlayerTrainingDeclineEvent save(PlayerTrainingDeclineEvent playerTrainingDeclineEvent);
}
