package com.kjeldsen.match.application.usecases;


import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchUseCase {

    private final MatchReadRepository matchReadRepository;

    public List<Match> getAll(String teamId, String leagueId) {
//        log.info("GetMatchUseCase for teamId={}, leagueId={}", teamId, leagueId);
        return leagueId != null ?  matchReadRepository.findMatchesByLeagueId(leagueId)
            : matchReadRepository.findMatchesByTeamId(teamId);
    }

    public Match get(String matchId) {
//        log.info("GetMatchUseCase for matchId={}", matchId);
        return matchReadRepository.findOneById(matchId)
            .orElseThrow(() -> new RuntimeException("Match not found"));
    }
}
