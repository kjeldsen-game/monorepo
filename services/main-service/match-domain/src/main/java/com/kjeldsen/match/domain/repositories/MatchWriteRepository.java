package com.kjeldsen.match.domain.repositories;

import com.kjeldsen.match.domain.entities.Match;

import java.util.List;

public interface MatchWriteRepository {

    Match save(Match match);

    List<Match> saveAll(List<Match> matches);
}
