package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerDeclineEvent;

public interface PlayerTrainingDeclineEventWriteRepository {
    PlayerDeclineEvent save(PlayerDeclineEvent playerDeclineEvent);
}
