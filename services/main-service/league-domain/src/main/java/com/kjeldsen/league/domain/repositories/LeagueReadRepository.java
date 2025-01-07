package com.kjeldsen.league.domain.repositories;

import com.kjeldsen.league.domain.League;

import java.util.List;
import java.util.Optional;

public interface LeagueReadRepository {
    Optional<League> findById(League.LeagueId leagueId);

    List<League> findAll();
}
