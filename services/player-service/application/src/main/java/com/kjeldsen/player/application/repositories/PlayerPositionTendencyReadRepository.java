package com.kjeldsen.player.application.repositories;

import com.kjeldsen.player.domain.PlayerPosition;
import com.kjeldsen.player.domain.PlayerPositionTendency;

import java.util.List;

public interface PlayerPositionTendencyReadRepository {
    PlayerPositionTendency get(PlayerPosition position);

    List<PlayerPositionTendency> find();
}
