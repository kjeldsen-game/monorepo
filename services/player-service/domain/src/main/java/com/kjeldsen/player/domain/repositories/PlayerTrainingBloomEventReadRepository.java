package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingBloomEvent;

import java.util.Optional;

public interface PlayerTrainingBloomEventReadRepository {

    Optional<PlayerTrainingBloomEvent> findOneByPlayerId(Player.PlayerId id);
}
