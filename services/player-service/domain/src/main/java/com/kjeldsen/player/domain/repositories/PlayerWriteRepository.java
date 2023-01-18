package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;

public interface PlayerWriteRepository {
    Player save(Player player);
}
