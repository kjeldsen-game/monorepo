package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.PlayerPositionTendency;

public interface PlayerPositionTendencyWriteRepository {
    PlayerPositionTendency save(PlayerPositionTendency playerPositionTendency);
}
