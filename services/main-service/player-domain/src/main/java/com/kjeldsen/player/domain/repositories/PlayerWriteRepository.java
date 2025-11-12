package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;

import java.util.List;

public interface PlayerWriteRepository {
    Player save(Player player);

    List<Player> saveAll(List<Player> players);
}
