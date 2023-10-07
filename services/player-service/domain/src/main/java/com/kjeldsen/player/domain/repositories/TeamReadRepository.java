package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.Team;

import java.util.Optional;

public interface TeamReadRepository {
    Optional<Team> findByUserId(String id);

    Optional<Team> findById(String id);
}
