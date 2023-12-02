package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.PlayerPotentialRiseEvent;
import com.kjeldsen.player.domain.events.PlayerTrainingEvent;

public interface PlayerPotentialRiseEventWriteRepository {
    PlayerPotentialRiseEvent save(PlayerPotentialRiseEvent playerPotentialRiseEvent);
}
