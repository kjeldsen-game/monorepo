package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;

import java.util.Optional;

public interface PlayerTrainingBloomEventReadRepository {

    Optional<PlayerTrainingBloomEvent> findOneByPlayerId(PlayerId id);
}
