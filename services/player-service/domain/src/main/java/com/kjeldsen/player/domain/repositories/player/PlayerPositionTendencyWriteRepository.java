package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.PlayerPositionTendency;

public interface PlayerPositionTendencyWriteRepository {
    PlayerPositionTendency save(PlayerPositionTendency playerPositionTendency);
}
