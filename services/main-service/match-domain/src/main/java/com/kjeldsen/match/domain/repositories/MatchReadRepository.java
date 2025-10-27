package com.kjeldsen.match.domain.repositories;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.stats.MatchStats;

import java.util.List;
import java.util.Optional;

public interface MatchReadRepository {

    Optional<Match> findOneById(String id);

    List<Match> findMatchesByTeamId(String teamId);

    List<Match> findMatchesByLeagueId(String leagueId);

    List<Match> findMatchesByLeagueIdAndTeamIdAndStatus(String teamId, String leagueId, Match.Status status);
}
