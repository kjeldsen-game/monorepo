package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.application.events.MatchCreatedEvent;
import com.kjeldsen.match.entities.Id;
import com.kjeldsen.match.entities.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchCreateUseCase {

    public void create(Team home, Team away) {
        log.info("Creating match for teams {} (home) and {} (away)", home.getId(), away.getId());

        MatchCreatedEvent.builder()
            .matchId(Id.generate())
            .homeTeamId(home.getId())
            .awayTeamId(away.getId())
            .build();
    }
}
