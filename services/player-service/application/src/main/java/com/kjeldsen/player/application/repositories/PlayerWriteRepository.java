package com.kjeldsen.player.application.repositories;

import com.kjeldsen.player.domain.Player;

public interface PlayerWriteRepository {
    Player save(Player player);
}
