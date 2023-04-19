package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingDeclineEvent;

import java.util.Optional;

public interface PlayerTrainingDeclineEventReadRepository {

    Optional<PlayerTrainingDeclineEvent> findOneByPlayerId(Player.PlayerId id);
}
