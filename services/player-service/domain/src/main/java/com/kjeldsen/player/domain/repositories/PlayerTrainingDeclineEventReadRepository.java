package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;

import java.util.Optional;

public interface PlayerTrainingDeclineEventReadRepository {

    Optional<PlayerTrainingDeclineEvent> findOneByPlayerId(PlayerId id);
}
