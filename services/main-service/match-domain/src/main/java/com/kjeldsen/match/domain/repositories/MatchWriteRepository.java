package com.kjeldsen.match.domain.repositories;

import com.kjeldsen.match.domain.entities.Match;

public interface MatchWriteRepository {

    Match save(Match match);
}
