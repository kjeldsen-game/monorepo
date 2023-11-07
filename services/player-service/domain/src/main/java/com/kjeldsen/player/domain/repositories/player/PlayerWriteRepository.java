package com.kjeldsen.player.domain.repositories.player;

import com.kjeldsen.player.domain.Player;

public interface PlayerWriteRepository {
    Player save(Player player);
}
