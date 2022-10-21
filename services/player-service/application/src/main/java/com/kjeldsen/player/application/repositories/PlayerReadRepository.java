package com.kjeldsen.player.application.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerId;

import java.util.List;
import java.util.Optional;

public interface PlayerReadRepository {
    Optional<Player> findOneById(PlayerId id);

    List<Player> find();
}
