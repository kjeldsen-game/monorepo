package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;

import java.util.List;

public interface PlayerPotentialRiseEventReadRepository {
    List<PlayerPotentialRiseEvent> findAllByPlayerId(Player.PlayerId playerId);
}
