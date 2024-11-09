package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingScheduledEvent;

import java.util.List;
import java.util.Optional;

public interface PlayerTrainingEventReadRepository {
    List<PlayerTrainingEvent> findAllByPlayerId(Player.PlayerId playerId);

    Optional<PlayerTrainingEvent> findLastByPlayerTrainingEvent(String playerTrainingEventId);
}
