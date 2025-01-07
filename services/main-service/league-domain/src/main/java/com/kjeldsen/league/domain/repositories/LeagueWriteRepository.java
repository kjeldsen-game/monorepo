package com.kjeldsen.league.domain.repositories;

import com.kjeldsen.league.domain.League;

public interface LeagueWriteRepository {
    League save(final League auction);
}
