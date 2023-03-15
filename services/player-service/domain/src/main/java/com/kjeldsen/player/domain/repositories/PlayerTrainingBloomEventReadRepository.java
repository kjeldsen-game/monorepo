package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerBloomEvent;

import java.util.Optional;

public interface PlayerTrainingBloomEventReadRepository {

    Optional<PlayerBloomEvent> findOneByPlayerId(PlayerId id);
}
