package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Player;

import java.util.List;
import java.util.Optional;

public interface PlayerReadRepository {
    Optional<Player> findOneById(Player.PlayerId id);

    List<Player> find(FindPlayersQuery query);
}
