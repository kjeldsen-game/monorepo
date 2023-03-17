package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerDeclineEvent;

import java.util.Optional;

public interface PlayerTrainingDeclineEventReadRepository {

    Optional<PlayerDeclineEvent> findOneByPlayerId(PlayerId id);
}
