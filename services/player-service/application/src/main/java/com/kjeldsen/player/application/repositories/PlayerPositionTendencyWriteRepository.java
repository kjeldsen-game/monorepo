package com.kjeldsen.player.application.repositories;

import com.kjeldsen.player.domain.PlayerPositionTendency;

public interface PlayerPositionTendencyWriteRepository {
    PlayerPositionTendency save(PlayerPositionTendency playerPositionTendency);
}
