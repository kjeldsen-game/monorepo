package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerId;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;

import java.util.List;

public interface PlayerTrainingEventReadRepository {
    List<PlayerTrainingEvent> findAllByPlayerId(PlayerId playerId);
}
