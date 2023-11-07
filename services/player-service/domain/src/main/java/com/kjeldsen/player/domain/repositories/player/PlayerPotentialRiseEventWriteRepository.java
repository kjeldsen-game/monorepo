package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;

public interface PlayerPotentialRiseEventWriteRepository {
    PlayerPotentialRiseEvent save(PlayerPotentialRiseEvent playerPotentialRiseEvent);
}
