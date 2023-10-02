package com.kjeldsen.match.application.usecases;

import com.kjeldsen.match.domain.aggregate.Match;
import com.kjeldsen.match.domain.event.MatchStartedEvent;
import com.kjeldsen.match.domain.id.MatchId;
import com.kjeldsen.match.engine.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchStartUseCase {

    public void start(MatchId matchId) {
        log.info("Starting match {}", matchId);

        validateMatchStart(matchId);

        MatchStartedEvent matchStartedEvent = MatchStartedEvent.builder()
            .matchId(MatchId.generate())
            .build();

        // TODO - save event

        Match match = Match.generate(matchStartedEvent);

        // Once the game is over, any resources (use cases, events) can be created and saved
        Game.play(match);

        // TODO - add an exception handler to catch game state exceptions
    }

    private void validateMatchStart(MatchId matchId) {
        // TODO
    }
}
