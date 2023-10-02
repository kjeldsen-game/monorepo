package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.aggregate.Team;
import com.kjeldsen.match.domain.event.MatchCreatedEvent;
import com.kjeldsen.match.domain.id.MatchId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchCreateUseCase {

    public void create(Team home, Team away) {
        log.info("Creating match for teams {} (home) and {} (away)", home.getId(), away.getId());

        validateMatchCreate(home, away);

        MatchCreatedEvent.builder()
            .matchId(MatchId.generate())
            .homeTeamId(home.getId())
            .awayTeamId(away.getId())
            .build();
    }

    private void validateMatchCreate(Team home, Team away) {
        // TODO - match between these teams already scheduled, etc.
    }
}
