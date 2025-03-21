package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.repositories.MatchReadRepository;
import com.kjeldsen.match.domain.repositories.MatchWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateMatchChallengeUseCase {

    private final GetMatchUseCase getMatchUseCase;
    private final MatchWriteRepository matchWriteRepository;

    public void update(String matchId, Match.Status status) {
        log.info("UpdateMatchChallengeUseCase for matchId={} status={}", matchId, status);
        Match match = getMatchUseCase.get(matchId);

        if (match.getStatus() != Match.Status.PENDING && !match.getAway().getId().equals(match.getHome().getId())) {
            throw new RuntimeException("Match is not in the PENDING status!");
        }

        match.setStatus(status);
        matchWriteRepository.save(match);
    }
}
