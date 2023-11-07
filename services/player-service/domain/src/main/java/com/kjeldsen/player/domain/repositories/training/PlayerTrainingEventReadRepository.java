package com.kjeldsen.player.domain.repositories.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;

import java.util.List;

public interface PlayerTrainingEventReadRepository {
    List<PlayerTrainingEvent> findAllByPlayerId(Player.PlayerId playerId);
}
