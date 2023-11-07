package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerCreationEvent;

import java.util.Optional;

public interface PlayerCreationEventReadRepository {
    Optional<PlayerCreationEvent> findByPlayerId(Player.PlayerId playerId);
}
