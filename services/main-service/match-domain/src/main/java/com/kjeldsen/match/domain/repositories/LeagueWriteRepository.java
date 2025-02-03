package com.kjeldsen.match.domain.repositories;

import com.kjeldsen.match.domain.entities.League;

public interface LeagueWriteRepository {
    League save(final League auction);
}

