package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.repositories.MatchReadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GetMatchesUseCase {

    private final MatchReadRepository matchReadRepository;

    public List<Match> get(String leagueId, String teamId) {
        return leagueId != null ? matchReadRepository.findMatchesByLeagueId(leagueId)
          : matchReadRepository.findMatchesByTeamId(teamId);
    }
}
