package com.kjeldsen.match.domain.repositories;

import com.kjeldsen.match.domain.entities.League;

import java.util.List;
import java.util.Optional;

public interface LeagueReadRepository {
    Optional<League> findById(League.LeagueId leagueId);

    List<League> findAll();

    List<League> findAllByStatus(League.LeagueStatus status);
}
